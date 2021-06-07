package com.exchange.exchangesearch.domain

import com.exchange.exchangesearch.application.service.ApiExchangeService
import com.exchange.exchangesearch.application.service.BaseService
import com.exchange.exchangesearch.infrastructure.outgoing.model.Transaction
import com.exchange.exchangesearch.infrastructure.outgoing.repository.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class ApiExchangeDomainService @Autowired constructor(
    private val apiExchangeService: ApiExchangeService,
    private val transactionRepository: TransactionRepository
) : BaseService() {

    companion object {
        const val FIELD_SORT_TRANSACTIONS = "transactionDate"
    }

    suspend fun getRates(
        userId: Long,
        originCurrency: String,
        destinationCurrency: String,
        value: Double
    ): Transaction {
        return try {
            val exchangeResponse = apiExchangeService.searchExchangeRates(userId, originCurrency, destinationCurrency)
            val conversionRate = exchangeResponse.rates.entries.first().value

            val transaction = Transaction(
                userId = userId,
                originCurrency = originCurrency,
                originValue = value,
                destinationCurrency = destinationCurrency,
                conversionRate = conversionRate,
                destinationValue = conversionRate * value
            )
            transactionRepository.save(transaction)

        } catch (ex: Exception) {
            throw handleUnexpectedException(ex)
        }
    }

    suspend fun getTransactionHistory(userId: Long, sort: Sort.Direction): List<Transaction> {
        return try {
            transactionRepository.findByUserId(userId, Sort.by(sort, FIELD_SORT_TRANSACTIONS))
        } catch (ex: Exception) {
            throw handleUnexpectedException(ex)
        }
    }
}