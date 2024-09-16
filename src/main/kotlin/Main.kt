import export.csv.saveNews
import integration.kudago.KudaGoClientService
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

suspend fun main() {
    //Четвертая таска - экспорт новостей в CSV
    val timeNow = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val kudaGoClientService = KudaGoClientService()
    val news = kudaGoClientService.getNews()
    saveNews("csv/test_news.csv", news)
}