package extensions

import data.News
import data.Place
import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertEquals

class NewsExtensionsTest {
    @Test
    fun testGetMostRatedNewsWithLoops() {
        val timeNow = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val news = listOf(
            News(1, timeNow, "Title 1", Place.PlaceObject(1), "Description 1", "url1", 10, 5),
            News(2, timeNow, "Title 2", Place.PlaceObject(2), "Description 2", "url2", 20, 10),
            News(3, timeNow, "Title 3", Place.PlaceObject(3), "Description 3", "url3", 5, 2)
        )

        // Установим период времени от 7 дней назад до сейчас
        val period = (timeNow.toJavaLocalDateTime().minusDays(7).toKotlinLocalDateTime()..timeNow)
        val result = news.getMostRatedNewsWithLoops(2, period)

        assertEquals(2, result.size)
        assertEquals(news[1], result[0]) // Ожидаем новость с id=2 на первом месте
        assertEquals(news[0], result[1]) // Ожидаем новость с id=1 на втором месте
    }

    @Test
    fun testGetMostRatedNewsWithSequences() {
        val timeNow = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val news = listOf(
            News(1, timeNow, "Title 1", Place.PlaceObject(1), "Description 1", "url1", 10, 5),
            News(2, timeNow, "Title 2", Place.PlaceObject(2), "Description 2", "url2", 20, 10),
            News(3, timeNow, "Title 3", Place.PlaceObject(3), "Description 3", "url3", 5, 2)
        )

        // Установим период времени от 7 дней назад до сейчас
        val period = (timeNow.toJavaLocalDateTime().minusDays(7).toKotlinLocalDateTime()..timeNow)
        val result = news.getMostRatedNewsWithSequences(2, period)

        assertEquals(2, result.size)
        assertEquals(news[1], result[0]) // Ожидаем новость с id=2 на первом месте
        assertEquals(news[0], result[1]) // Ожидаем новость с id=1 на втором месте
    }
}