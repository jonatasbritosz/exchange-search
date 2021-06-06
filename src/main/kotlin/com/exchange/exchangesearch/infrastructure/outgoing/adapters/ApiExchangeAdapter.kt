package com.exchange.exchangesearch.infrastructure.outgoing.adapters

import com.exchange.exchangesearch.infrastructure.outgoing.model.ExchangeResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class ApiExchangeAdapter(
    @Value("\${exchange.api.base-url}") private val baseUrl: String
) {

    private val webClientBuilder = WebClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)

    suspend fun searchExchangeRates(
        queryParams: MultiValueMap<String, String>
    ): ExchangeResponse =
        webClientBuilder.build()
            .get()
            .uri { builder ->
                builder.queryParams(queryParams).build()
            }
            .retrieve()
            .awaitBody()
}