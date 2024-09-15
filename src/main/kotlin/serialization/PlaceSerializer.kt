package serialization

import data.Place
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object PlaceSerializer : KSerializer<Place> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Place")

    override fun deserialize(decoder: Decoder): Place {
        val input = decoder as? JsonDecoder ?: throw SerializationException("Expected JsonDecoder")
        val jsonElement = input.decodeJsonElement()

        return when {
            jsonElement is JsonPrimitive && jsonElement.isString -> {
                Place.PlaceString(jsonElement.content)
            }
            jsonElement is JsonObject -> {
                val id = jsonElement["id"]?.jsonPrimitive?.int ?: throw SerializationException("Invalid Place object")
                Place.PlaceObject(id)
            }
            else -> throw SerializationException("Invalid Place format")
        }
    }

    override fun serialize(encoder: Encoder, value: Place) {
        val output = encoder as? JsonEncoder ?: throw SerializationException("Expected JsonEncoder")
        when (value) {
            is Place.PlaceString -> output.encodeString(value.name)
            is Place.PlaceObject -> {
                val jsonObject = buildJsonObject {
                    put("id", value.id)
                }
                output.encodeJsonElement(jsonObject)
            }
        }
    }
}