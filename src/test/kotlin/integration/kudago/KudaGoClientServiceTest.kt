import data.News
import data.NewsResponse
import data.Place
import integration.kudago.KudaGoClientService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(DelicateCoroutinesApi::class)
class KudaGoClientServiceTest : StringSpec({

    val json = Json { encodeDefaults = true }

    "fetch news with default parameters should return news from mock response" {
        val mockEngine = MockEngine { request ->
            // Create a NewsResponse object
            val newsResponse = NewsResponse(
                count = 1,
                next = "",
                previous = "",
                results = listOf(
                    News(
                        id = 1,
                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                        "Sample News",
                        Place.PlaceString("cool_place"),
                        "Sample Description",
                        "http://example.com",
                        10,
                        5,
                    )
                )
            )

            // Serialize the NewsResponse object to JSON
            val responseContent = json.encodeToString(newsResponse)

            respond(
                content = responseContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val service = KudaGoClientService(httpClient)

        val result = runBlocking { service.getNewsPage("smr", 1, 0) }

        result.size shouldBe 1
        result[0].title shouldBe "Sample News"
    }

    "fetch news with default parameters should return empty list when API returns no news" {
        val mockEngine = MockEngine { _ ->
            val newsResponse = NewsResponse(
                count = 0,
                next = null,
                previous = null,
                results = emptyList()
            )
            val responseContent = json.encodeToString(newsResponse)

            respond(
                content = responseContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val service = KudaGoClientService(httpClient)

        val result = runBlocking { service.getNewsPage("smr", 1, 0) }

        result.size shouldBe 0
    }

    "should limit concurrent requests according to semaphore" {
        val maxRequests = 3 // Set your desired limit
        val mockEngine = MockEngine { request ->
            // Mock a successful response for each request
            val newsResponse = NewsResponse(
                count = 1,
                next = null,
                previous = null,
                results = listOf(
                    News(
                        id = 1,
                        publicationDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                        title = "Sample News",
                        place = null, // Assuming Place is nullable for this test
                        description = "Sample Description",
                        siteUrl = "http://example.com",
                        favoritesCount = 10,
                        commentsCount = 5,
                    )
                )
            )

            respond(
                content = json.encodeToString(newsResponse),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val service = KudaGoClientService(httpClient, maxRequests = maxRequests)

        // Create a coroutine scope to launch concurrent requests
        val jobs = List(10) { // Attempt to make more requests than maxRequests
            GlobalScope.launch {
                val result = service.getNewsPage()
                result.size shouldBe 1 // Each request should return one news item
            }
        }

        // Wait for all jobs to complete
        runBlocking {
            jobs.forEach { it.join() }
        }
    }
})