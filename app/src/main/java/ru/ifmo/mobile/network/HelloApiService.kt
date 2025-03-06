package ru.ifmo.mobile.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable


class HelloApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun fetchHello(): HelloResponse {
        // Подставляем свой URL от Mocky.io
        return client.get("https://run.mocky.io/v3/d18ffd60-61d0-4f45-82e4-394f628f6b97").body()
    }
}

@Serializable
data class HelloResponse(val message: String)