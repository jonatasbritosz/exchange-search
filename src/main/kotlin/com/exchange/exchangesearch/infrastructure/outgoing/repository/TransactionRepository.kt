package com.exchange.exchangesearch.infrastructure.outgoing.repository

import com.exchange.exchangesearch.infrastructure.outgoing.model.Transaction
import org.springframework.data.domain.Sort
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : CoroutineSortingRepository<Transaction, Long> {
    suspend fun findByUserId(userId: Long, sort: Sort): List<Transaction>
}