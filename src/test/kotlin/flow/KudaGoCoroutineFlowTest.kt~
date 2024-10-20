package flow

import data.News
import data.Place
import dsl.NewsPrinter
import integration.kudago.KudaGoClientService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.File


class KudaGoCoroutineFlowTest : StringSpec({
    beforeTest {
        val newsFile = File("test.md")
        if (newsFile.exists()) {
            newsFile.delete()
        }
    }
    "flow.start() should successfully retrieve all news and write them to a file" {
        val mockClient = mockk<KudaGoClientService>()
        val totalNewsCount = 4
        val workerCount = 2
        val maxPageSize = 2
        coEvery { mockClient.getNewsPage("smr", 2, 0) } returns listOf(
            News(
                1, Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()), "news 1",
                Place.PlaceString("place 1"), "description 1", "https://example.com/1", 1, 1
            ),
            News(
                2, Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()), "news 2",
                Place.PlaceString("place 2"), "description 2", "https://example.com/2", 1, 1
            ),
        )
        coEvery { mockClient.getNewsPage("smr", 2, 1) } returns listOf(
            News(
                3, Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()), "news 3",
                Place.PlaceString("place 3"), "description 3", "https://example.com/3", 1, 1
            ),
            News(
                4, Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()), "news 4",
                Place.PlaceString("place 4"), "description 4", "https://example.com/4", 1, 1
            ),
        )
        coEvery { mockClient.getNewsPage("smr", 2, 2) } returns listOf(
            News(
                3, Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()), "news 5",
                Place.PlaceString("place 5"), "description 5", "https://example.com/5", 1, 1
            ),
            News(
                4, Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()), "news 6",
                Place.PlaceString("place 6"), "description 6", "https://example.com/6", 1, 1
            ),
        )
        val flow = KudaGoCoroutineFlow(
            mockClient,
            NewsPrinter(),
            totalNewsCount,
            workerCount,
            maxPageSize
        )
        runBlocking {
            flow.start()
        }
        val newsFile = File("test.md")
        newsFile.exists() shouldBe true
        coVerify(exactly = 1) { mockClient.getNewsPage(pageNumber = 0, elementsInPageNumber = 2) }
        coVerify(exactly = 1) { mockClient.getNewsPage(pageNumber = 1, elementsInPageNumber = 2) }
        coVerify(exactly = 1) { mockClient.getNewsPage(pageNumber = 2, elementsInPageNumber = 2) }
        confirmVerified(mockClient)
    }

    "flow.start() should handle empty news response correctly" {
        val mockClient = mockk<KudaGoClientService>()
        val totalNewsCount = 0 // No news items expected in this scenario
        val workerCount = 2
        val maxPageSize = 2

        // Mocking responses to return empty lists for all pages
        coEvery { mockClient.getNewsPage("smr", 2, 0) } returns emptyList()
        coEvery { mockClient.getNewsPage("smr", 2, 1) } returns emptyList()
        coEvery { mockClient.getNewsPage("smr", 2, 2) } returns emptyList()

        val flow = KudaGoCoroutineFlow(
            mockClient,
            NewsPrinter(),
            totalNewsCount,
            workerCount,
            maxPageSize
        )

        runBlocking {
            flow.start()
        }

        // Check if the file is created even when there are no news items.
        val newsFile = File("test.md")
        newsFile.exists() shouldBe true

        // Verify that the client was called for each page, even though they returned empty lists.
        coVerify(exactly = 1) { mockClient.getNewsPage("smr", 2, 0) }

        confirmVerified(mockClient)
    }
})
