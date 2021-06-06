package com.exchange.exchangesearch.infrastructure.outgoing.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExchangeResponse(
	val success: Boolean? = null,
	val timestamp: Long? = null,
	val date: LocalDate? = null,
	val base: String? = null,
	val rates: Map<String, Double> = emptyMap(),
	val error: Error? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Error(
	val code: Int,
	val type: String
)