package integration.kudago

import data.News
import data.NewsResponse
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class KudaGoClientService(
    private val client: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    },
    private val json: Json = Json { ignoreUnknownKeys = true },
    private val apiVersion: String = "v1.4",
    private val baseUrl: String = "https://kudago.com/public-api/$apiVersion/news/",
    private val logger: Logger = LoggerFactory.getLogger(KudaGoClientService::class.java),
    private val maxRequests: Int = 5,
    private val semaphore: Semaphore = Semaphore(maxRequests)
) {
    companion object {
        private const val DEFAULT_LOCATION = "smr"
        private const val DEFAULT_COUNT = 100
        private const val DEFAULT_PAGE = 1
        private const val FIELDS = "id,title,place,description,site_url,favorites_count,comments_count,publication_date"
    }
    suspend fun getNewsPage(
        location: String = DEFAULT_LOCATION,
        elementsInPageNumber: Int = DEFAULT_COUNT,
        pageNumber: Int = DEFAULT_PAGE
    ): List<News> {
        return semaphore.withPermit {
            try {
                val response: HttpResponse = client.get(baseUrl) {
                    parameter("location", location)
                    parameter("text_format", "text")
                    parameter("fields", FIELDS)
                    parameter("page_size", elementsInPageNumber)
                    parameter("page", pageNumber)
                    parameter("order_by", "-publication_date")
                }

                val news: NewsResponse = json.decodeFromString(response.bodyAsText())
                logger.debug("Fetched List of news: {}", news)
                logger.info("Successfully fetched ${news.results.size} news from KudaGo")
                return news.results
            } catch (e: Exception) {
                logger.error("Error fetching news: ${e.message}")
                emptyList()
            }
        }
    }
}
