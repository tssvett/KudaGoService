package data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import serialization.LocalDateTimeSerializer
import serialization.PlaceSerializer
import kotlin.math.exp

@Serializable
data class News(
    @SerialName("id") val id: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("publication_date") val publicationDate: LocalDateTime,
    @SerialName("title") val title: String,
    @Serializable(with = PlaceSerializer::class)
    @SerialName("place") val place: Place?,
    @SerialName("description") val description: String,
    @SerialName("site_url") val siteUrl: String,
    @SerialName("favorites_count") val favoritesCount: Int,
    @SerialName("comments_count") val commentsCount: Int
) {
    val rating: Double
        get() = 1 / (1 + exp(-(favoritesCount.toDouble() / (commentsCount + 1))))
}
