package dataclasses

import kotlin.math.exp

data class News(
    val id: Int,
    val title: String,
    val place: String,
    val description: String,
    val siteUrl: String,
    val favoritesCount: Int,
    val commentsCount: Int
) {
    val rating: Double
        get() = 1 / (1 + exp(-(favoritesCount.toDouble() / (commentsCount + 1))))
}
