import data.News
import dsl.newsOutputDsl
import export.csv.saveNews
import integration.kudago.KudaGoClientService
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

suspend fun main() {
    task4()
    task5()
}

suspend fun task4() {
    val kudaGoClientService = KudaGoClientService()
    val news = kudaGoClientService.getNews()
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