package export.csv

import data.News
import org.slf4j.LoggerFactory
import java.io.File
import java.io.PrintWriter

fun saveNews(path: String, news: Collection<News>) {
    val logger = LoggerFactory.getLogger("CsvExport")
    val file = File(path)
    logger.info("Starting export news to $path")
    require(file.parentFile != null) { "Указанный путь не имеет родительского каталога: $path" }
    if (!file.parentFile.exists()) {
        file.parentFile.mkdirs()
    }

    require(!file.exists()) { "Файл уже существует по указанному пути: $path" }
    PrintWriter(file).use { writer ->
        writer.println("id,title,place_id,description,site_url,favorites_count,comments_count,publication_date")
        news.forEach { newsItem ->
            writer.println("${newsItem.id},${newsItem.title},${newsItem.place},${newsItem.description},${newsItem.siteUrl},${newsItem.favoritesCount},${newsItem.commentsCount},${newsItem.publicationDate}")
        }
    }
    logger.info("Successfully exported ${news.size} news to .csv in: $path")
}