package com.exchange.exchangesearch.application.service

import com.exchange.exchangesearch.BaseTest
import com.exchange.exchangesearch.application.exception.BadRequestException
import com.exchange.exchangesearch.infrastructure.outgoing.adapters.ApiExchangeAdapter
import com.exchange.exchangesearch.infrastructure.outgoing.model.ExchangeResponse
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.time.Instant
import java.time.LocalDate

class ApiExchangeServiceUnitTest(
    @Autowired
    private val apiExchangeService: ApiExchangeService
) : BaseTest() {

    @MockBean
    private lateinit var apiExchangeAdapter: ApiExchangeAdapter

    private val now = Instant.now()

    private val apiSuccessResponse = ExchangeResponse(
        success = true,
        timestamp = now.toEpochMilli(),
        date = LocalDate.now(),
        base = "EUR",
        rates = mapOf("BRL" to 6.142633)
    )

    @Test
    fun `WHEN searchExchangeRates, GIVEN all parameters are valid, THEN process without erros`() {
        runBlocking {
            whenever(apiExchangeAdapter.searchExchangeRates(any())).thenReturn(apiSuccessResponse)

            apiExchangeService.searchExchangeRates(123L, "EUR", "BRL")

            verify(apiExchangeAdapter, times(1)).searchExchangeRates(any())
        }
    }

    @Test
    fun `WHEN validateParams, GIVEN searchExchangeRates returns exception, THEN throws WebClientResponseException`() {
        runBlocking {
            assertThrows<WebClientResponseException> {
                whenever(apiExchangeAdapter.searchExchangeRates(any())).thenThrow(
                    WebClientResponseException(500, "error", null, null, null)
                )
                apiExchangeService.searchExchangeRates(123L, "EUR", "BRL")
            }
        }
    }

    @Test
    fun `WHEN validateParams, GIVEN originCurrency is blank, THEN throws BadRequestException`() {
        runBlocking {
            assertThrows<BadRequestException> {
                apiExchangeService.validateParams(
                    originCurrency = "",
                    destinationCurrency = "BRL",
                    queryParams = LinkedMultiValueMap()
                )
            }
        }
    }

    @Test
    fun `WHEN validateParams, GIVEN destinationCurrency is blank, THEN throws BadRequestException`() {
        runBlocking {
            assertThrows<BadRequestException> {
                apiExchangeService.validateParams(
                    originCurrency = "BRL",
                    destinationCurrency = "",
                    queryParams = LinkedMultiValueMap()
                )
            }
        }
    }

    @Test
    fun `WHEN validateParams, GIVEN originCurrency is not supported, THEN throws BadRequestException`() {
        runBlocking {
            assertThrows<BadRequestException> {
                apiExchangeService.validateParams(
                    originCurrency = "ZZZ",
                    destinationCurrency = "BRL",
                    queryParams = LinkedMultiValueMap()
                )
            }
        }
    }

    @Test
    fun `WHEN validateParams, GIVEN destinationCurrency is not supported, THEN throws BadRequestException`() {
        runBlocking {
            assertThrows<BadRequestException> {
                apiExchangeService.validateParams(
                    originCurrency = "BRL",
                    destinationCurrency = "DSU",
                    queryParams = LinkedMultiValueMap()
                )
            }
        }
    }

}