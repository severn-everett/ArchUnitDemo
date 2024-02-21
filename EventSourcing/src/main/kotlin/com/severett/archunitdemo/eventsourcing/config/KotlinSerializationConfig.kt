package com.severett.archunitdemo.eventsourcing.config

import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder

@Configuration
class KotlinSerializationConfig {
    private val json = Json { ignoreUnknownKeys = true }

    @Bean
    fun encoder() = KotlinSerializationJsonEncoder(json)

    @Bean
    fun decoder() = KotlinSerializationJsonDecoder(json)
}
