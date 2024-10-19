import data.News
import dsl.NewsPrinter
import dsl.newsOutputDsl
import export.csv.saveNews
import flow.KudaGoCoroutineFlow
import integration.kudago.KudaGoClientService
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

suspend fun main() {
    task6()
}

suspend fun task4() {
    val kudaGoClientService = KudaGoClientService()
    val news = kudaGoClientService.getNewsPage()
    saveNews("csv/test_news.csv", news)
}

fun task5() {
    val news1 = News(
        1,
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        "Kotlin DSL in Action",
        null,
        "Learn how to create a DSL in Kotlin.",
        "https://kotlinlang.org/docs/type-safe-builders.html",
        10,
        2
    )

    val news2 = News(
        1,
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        "Kotlin DSL in Action",
        null,
        "Learn how to create a DSL in Kotlin.",
        "https://kotlinlang.org/docs/type-safe-builders.html",
        10,
        2
    )
    newsOutputDsl(4) {
        news(listOf(news1, news2))
    }
}

suspend fun task6() {
    KudaGoCoroutineFlow(
        kudaGoClientService = KudaGoClientService(),
        newsPrinter = NewsPrinter(),
        totalNewsCount = 100,
        workerCount = 2,
        pageSize = 10
    ).start()
}