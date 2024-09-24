package export.csv

import data.News
import data.Place
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.*
import java.io.File

class CsvExportTest {

    private val testDir = "test" // Директория для тестов
    private val testFilePath = "$testDir/test_news.csv"

    @BeforeEach
    fun setup() {
        // Создаем директорию для тестов, если она не существует
        File(testDir).mkdirs()
    }

    @AfterEach
    fun teardown() {
        // Удаляем файл после каждого теста, если он существует
        File(testFilePath).delete()
    }

    @Test
    fun `test saveNews creates file correctly`() {
        // Arrange
        val newsList = listOf(
            News(
                1,
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                "Title 1",
                Place.PlaceObject(1),
                "Description 1",
                "url1",
                10,
                5
            ),
            News(
                2,
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                "Title 2",
                Place.PlaceObject(2),
                "Description 2",
                "url2",
                20,
                10
            ),
            News(
                3,
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                "Title 3",
                Place.PlaceObject(3),
                "Description 3",
                "url3",
                5,
                2
            )
        )

        // Act
        saveNews(testFilePath, newsList)

        // Assert: Проверяем, что файл был создан и содержит правильные данные
        val file = File(testFilePath)
        Assertions.assertTrue(file.exists(), "Файл не был создан")

        val fileContent = file.readText()
        Assertions.assertTrue(fileContent.contains("id,title,place_id,description,site_url,favorites_count,comments_count,publication_date"))
        Assertions.assertTrue(fileContent.contains("1,Title 1,PlaceObject(id=1),Description 1,url1,10,5"))
        Assertions.assertTrue(fileContent.contains("2,Title 2,PlaceObject(id=2),Description 2,url2,20,10"))
        Assertions.assertTrue(fileContent.contains("3,Title 3,PlaceObject(id=3),Description 3,url3,5,2"))
    }

    @Test
    fun `test saveNews throws exception if file exists`() {
        // Arrange: Создаем файл заранее
        File(testFilePath).createNewFile()

        // Act & Assert: Проверяем выброс исключения при попытке перезаписи существующего файла
        val exception = assertThrows<IllegalArgumentException> {
            saveNews(testFilePath, emptyList())
        }
        Assertions.assertEquals("Файл уже существует по указанному пути: $testFilePath", exception.message)
    }

    @Test
    fun `test saveNews throws exception if parent directory does not exist`() {
        // Arrange: Указываем путь к файлу в несуществующей директории
        val invalidPath = "/invalid/directory/test_news.csv"

        // Act & Assert: Проверяем выброс исключения при отсутствии родительской директории
        val exception = assertThrows<IllegalArgumentException> {
            saveNews(invalidPath, emptyList())
        }
        Assertions.assertEquals("Файл уже существует по указанному пути: $invalidPath", exception.message)
    }
}