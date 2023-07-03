package com.example.mymealsapp.api

import com.example.mymealsapp.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.http.path
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorApiImpl : KtorApi {

    val baseUrl = BuildKonfig.baseUrl

    private val jsonConfiguration
        get() = Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }

    override val client: HttpClient
        get() = HttpClient {
            install(ContentNegotiation) {
                json(jsonConfiguration)
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }

    override fun HttpRequestBuilder.apiUrl(path: String, parameterKey: String, parameterValue: String) {
        url {
            takeFrom(baseUrl)
            path("api", path)
            parameter(parameterKey, parameterValue)
        }
    }
}