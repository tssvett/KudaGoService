suspend fun main() {
    val client = KudaGoClientService()
    val news = client.getNews()
    news.forEach() { println(it) }
}