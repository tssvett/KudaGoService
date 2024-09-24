package data

import kotlinx.serialization.Serializable

@Serializable
sealed class Place {
    @Serializable
    data class PlaceObject(val id: Int) : Place()

    @Serializable
    data class PlaceString(val name: String) : Place()
}
