package extensions

import data.News
import kotlinx.datetime.LocalDateTime

fun List<News>.getMostRatedNewsWithLoops(count: Int, period: ClosedRange<LocalDateTime>): List<News> {
    val filteredNews = this.filter { it.publicationDate in period }
    val sortedNews = filteredNews.sortedByDescending { it.rating }
    return sortedNews.take(count)
}

fun List<News>.getMostRatedNewsWithSequences(count: Int, period: ClosedRange<LocalDateTime>): List<News> {
    return this.asSequence()
        .filter { it.publicationDate in period }
        .sortedByDescending { it.rating }
        .take(count)
        .toList()
}
