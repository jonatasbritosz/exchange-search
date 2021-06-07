package com.exchange.exchangesearch.infrastructure.incoming

import com.exchange.exchangesearch.domain.ApiExchangeDomainService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rates")
class RatesRestAdapter @Autowired constructor(
    private val apiExchangeDomainService: ApiExchangeDomainService
) {

    @GetMapping
    suspend fun getRates(
        @RequestParam userId: Long,
        @RequestParam originCurrency: String,
        @RequestParam destinationCurrency: String,
        @RequestParam value: Double,
    ) = apiExchangeDomainService.getRates(userId, originCurrency, destinationCurrency, value)

    @GetMapping("/transactions-history")
    suspend fun getTransactionHistory(
        @RequestParam userId: Long,
        @RequestParam(required = false, defaultValue = "ASC") sort: Sort.Direction
    ) = apiExchangeDomainService.getTransactionHistory(userId, sort)

}