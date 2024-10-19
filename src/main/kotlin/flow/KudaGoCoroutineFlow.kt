package flow

import data.News
import dsl.NewsPrinter
import integration.kudago.KudaGoClientService
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class KudaGoCoroutineFlow(
    private val kudaGoClientService: KudaGoClientService,
    private val newsPrinter: NewsPrinter,
    private val totalNewsCount: Int,
    private val workerCount: Int,
    private val pageSize: Int
) {
    private val logger: Logger = LoggerFactory.getLogger(KudaGoCoroutineFlow::class.java)
    private val channel = Channel<News>(Channel.UNLIMITED)

    @OptIn(DelicateCoroutinesApi::class)
    private val threadPool = newFixedThreadPoolContext(workerCount, "workerPool")

    suspend fun start() = coroutineScope {
        val workers = List(workerCount) { workerId ->
            launch(threadPool) { fetchNews(workerId) }
        }

        val processor = launch { processNews() }

        workers.joinAll()
        channel.close()

        processor.join()
    }

    private suspend fun fetchNews(workerId: Int) {
        val totalPages = totalNewsCount / pageSize

        for (page in workerId..totalPages step workerCount) {
            logger.info("Worker $workerId: Fetching page $page")
            kudaGoClientService.getNewsPage(count = pageSize, page = page).forEach { channel.send(it) }
        }
    }

    private suspend fun processNews() {
        File("test.md").bufferedWriter().use { writer ->
            for (news in channel) {
                writer.write(newsPrinter.news(news))
                writer.newLine()
            }
        }
    }
}