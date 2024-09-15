suspend fun main() {
    val client = KudaGoServiceClient()
    val news = client.getNews()
    news.forEach() { println(it) }
}