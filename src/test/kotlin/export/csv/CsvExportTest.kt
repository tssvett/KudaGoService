package export.csv

import data.News
import data.Place
import io.mockk.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class CsvExportTest {

    @BeforeEach
    fun setup() {
        mockkStatic(File::class)
    }

    @AfterEach
    fun teardown() {
        unmockkStatic(File::class)
    }

    @Test
    fun `test saveNews creates file correctly`() {
        val path = "test_news.csv"
        val file = mockFile(path, true)
        val timeNow = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val newsList = listOf(
            News(1, timeNow, "Title 1", Place.PlaceObject(1), "Description 1", "url1", 10, 5),
            News(2, timeNow, "Title 2", Place.PlaceObject(2), "Description 2", "url2", 20, 10),
            News(3, timeNow, "Title 3", Place.PlaceObject(3), "Description 3", "url3", 5, 2)
        )

        saveNews(path, newsList)

        verify { file.exists() }
        verify { file.parentFile.exists() }
        verify { file.writeText(match { it.contains("id,title,place,description,site_url,favorites_count,comments_count,publication_date") }) }
        verify { file.writeText(match { it.contains("1,Title 1,1,Description 1,url1,10,5") }) }
        verify { file.writeText(match { it.contains("2,Title 2,2,Description 2,url2,20,10") }) }
    }

    @Test
    fun `test saveNews throws exception if file exists`() {
        val path = "existing_file.csv"
        val file = mockFile(path, true)

        assertThrows<IllegalArgumentException> {
            saveNews(path, emptyList())
        }

        verify { file.exists() }
    }

    @Test
    fun `test saveNews throws exception if parent directory does not exist`() {
        val path = "/invalid/directory/test_news.csv"
        val file = mockFile(path, false)

        assertThrows<IllegalArgumentException> {
            saveNews(path, emptyList())
        }

        verify { file.parentFile.exists() }
    }

    private fun mockFile(path: String, parentExists: Boolean): File {
        val file = mockk<File>(relaxed = true)
        val parent = mockk<File>(relaxed = true)

        every { File(path) } returns file
        every { file.parentFile } returns parent
        every { parent.exists() } returns parentExists

        return file
    }
}