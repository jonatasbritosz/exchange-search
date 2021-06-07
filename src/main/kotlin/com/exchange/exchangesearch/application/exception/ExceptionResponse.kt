package com.exchange.exchangesearch.application.exception

import java.time.Instant

data class ExceptionResponse(
    val timestamp: Instant,
    val path: String,
    val status: String,
    val error: String,
    val message: String,
    val requestId: String,
)
