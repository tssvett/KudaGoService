package dsl

import data.News
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NewsPrinter(
    private var indentSpaces: Int = 4,
    private var logger: Logger = LoggerFactory.getLogger(NewsPrinter::class.java)
) {

    // Метод для вывода одной новости
    fun news(newsItem: News) {
        printNews(newsItem)
    }

    // Метод для вывода списка новостей
    fun news(newsItems: List<News>) {
        newsItems.forEach { printNews(it) }
    }

    private fun printNews(newsItem: News) {
        logger.debug("Starting output with dsl news item: {}", newsItem)
        val indent = " ".repeat(indentSpaces)

        println(
            """
            |${indent}News ID: ${newsItem.id}
            |${indent}Title: ${newsItem.title}
            |${indent}Description: ${newsItem.description}
            |${indent}Publication Date: ${newsItem.publicationDate}
            |${indent}Place: ${newsItem.place?.toString() ?: "N/A"}
            |${indent}Site URL: ${newsItem.siteUrl}
            |${indent}Favorites Count: ${newsItem.favoritesCount}
            |${indent}Comments Count: ${newsItem.commentsCount}
            |${indent}Rating: ${"%.2f".format(newsItem.rating)}
        """.trimMargin()
        )

        println("${indent}-----------------------------")
    }
}

fun newsOutputDsl(indentSpaces: Int = 4, block: NewsPrinter.() -> Unit) {
    val printer = NewsPrinter(indentSpaces)
    printer.block()
}