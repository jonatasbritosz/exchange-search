package com.exchange.exchangesearch.infrastructure.outgoing.model

import org.springframework.data.annotation.Id
import java.time.Instant

data class Transaction(
    @Id
    val id: Long? = null,
    val userId: Long,
    val originCurrency: String,
    var originValue: Double,
    val destinationCurrency: String,
    var destinationValue: Double,
    val conversionRate: Double,
    val transactionDate: Instant = Instant.now()
)
