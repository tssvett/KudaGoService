package integration.kudago

import data.News
import data.NewsResponse
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
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
    private val url: String = "https://kudago.com/public-api/$apiVersion/news/",
    private val logger: Logger = LoggerFactory.getLogger(KudaGoClientService::class.java.name)
) {
    companion object {
        private const val DEFAULT_LOCATION = "smr"
        private const val DEFAULT_COUNT = 100
        private const val DEFAULT_PAGE = 1
        private const val FIELDS = "id,title,place,description,site_url,favorites_count,comments_count,publication_date"
    }

    suspend fun getNews(
        location: String = DEFAULT_LOCATION,
        count: Int = DEFAULT_COUNT,
        page: Int = DEFAULT_PAGE
    ): List<News> {
        return try {
            val response: HttpResponse = client.get(url) {
                parameter("location", location)
                parameter("text_format", "text")
                parameter("fields", FIELDS)
                parameter("page_size", count)
                parameter("page", page)
                parameter("order_by", "-publication_date")
            }
            val news: NewsResponse = json.decodeFromString(response.bodyAsText())
            logger.debug("Fetched List of news: {}", news)
            logger.info("Successfully fetched ${news.results.size} news from KudaGo")
            news.results
        } catch (e: Exception) {
            logger.error("Error fetching news: ${e.message}")
            emptyList()
        }
    }
}
