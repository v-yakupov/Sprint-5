package ru.sber.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JsonCustomClassDeserializer {

    @Test
    fun `Нобходимо десериализовать данные в класс`() {
        // given
        val data = """{"client": "Иванов Иван Иванович"}"""
        val objectMapper = ObjectMapper()
            .registerModules(KotlinModule(), SimpleModule().addDeserializer(Client7::class.java, Client7Deserializer()))

        // when
        val client = objectMapper.readValue<Client7>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
    }

    class Client7Deserializer : StdDeserializer<Client7>(Client7::class.java) {
        override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): Client7 {
            val value = p0?.codec?.readTree<JsonNode>(p0)?.get("client")?.textValue()?.split(" ")
            return Client7(
                firstName = value?.get(1) ?: "",
                lastName = value?.get(0) ?: "",
                middleName = value?.get(2) ?: ""
            )
        }
    }
}
