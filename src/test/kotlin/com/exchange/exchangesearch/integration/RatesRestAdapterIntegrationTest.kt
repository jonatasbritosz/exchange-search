package com.exchange.exchangesearch.integration

import com.exchange.exchangesearch.BaseIntegrationTest
import com.exchange.exchangesearch.infrastructure.incoming.RatesRestAdapter
import com.exchange.exchangesearch.infrastructure.outgoing.model.Transaction
import com.exchange.exchangesearch.infrastructure.outgoing.repository.TransactionRepository
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Instant

class RatesRestAdapterIntegrationTest(
    @Autowired
    private val mockWebServer: MockWebServer,
    @Autowired
    private val ratesRestAdapter: RatesRestAdapter,
    @Autowired
    private val transactionRepository: TransactionRepository
) : BaseIntegrationTest() {

    private var webTestClient: WebTestClient = WebTestClient
        .bindToController(ratesRestAdapter)
        .build()

    private val now = Instant.now()

    private val transactions = listOf(
        Transaction(
            userId = 111,
            originCurrency = "BRL",
            originValue = 10.0,
            destinationCurrency = "USD",
            conversionRate = 1.8,
            destinationValue = 18.0,
            transactionDate = now.plusSeconds(1)
        ),
        Transaction(
            userId = 111,
            originCurrency = "BRL",
            originValue = 10.0,
            destinationCurrency = "JPY",
            conversionRate = 0.12,
            destinationValue = 1.12,
            transactionDate = now.plusSeconds(2)
        ),
        Transaction(
            userId = 111,
            originCurrency = "BRL",
            originValue = 10.0,
            destinationCurrency = "GBP",
            conversionRate = 2.32,
            destinationValue = 23.2,
            transactionDate = now.plusSeconds(3)
        )
    )


    @Test
    fun `GIVEN valid params, WHEN getRates, THEN returns the current rate`() {

        val exchangeApiResponse = getResourceAsString("/success_response.json")
        val mockedExchangeApiResponse = MockResponse().setBody(exchangeApiResponse)
            .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setResponseCode(HttpStatus.OK.value())
        mockWebServer.enqueue(mockedExchangeApiResponse)

        val response = webTestClient.get()
            .uri("/rates?userId=123&originCurrency=BRL&destinationCurrency=USD&value=2.0")
            .exchange()
            .expectStatus().isOk
            .expectBody(Transaction::class.java)
            .returnResult()
            .responseBody!!

        val authRequest = mockWebServer.takeRequest()
        Assertions.assertEquals("GET", authRequest.method)
        Assertions.assertEquals("/?base=BRL&symbols=USD&access_key=b764d032546c9425cbca589b7c8dbf7a", authRequest.path)

        Assertions.assertNotNull(response.id)
        Assertions.assertEquals(response.originCurrency, "BRL")
        Assertions.assertEquals(response.destinationCurrency, "USD")
    }

    @Test
    fun `GIVEN valid params, WHEN getRates and the external service return a exception, THEN returns BadRequestException`() {

        val exchangeApiResponse = getResourceAsString("/error_response.json")
        val mockedExchangeApiResponse = MockResponse().setBody(exchangeApiResponse)
            .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setResponseCode(HttpStatus.BAD_REQUEST.value())
        mockWebServer.enqueue(mockedExchangeApiResponse)

        webTestClient.get()
            .uri("/rates?userId=7585&originCurrency=BRL&destinationCurrency=USD&value=2.0")
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `GIVEN invalid params, WHEN getRates, THEN returns BadRequestException`() {
        webTestClient.get()
            .uri("/rates?userId=7585&originCurrency=ZZZ&destinationCurrency=USD&value=2.0")
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `GIVEN valid params, WHEN getTransactionHistory, THEN returns a list of transactions`() {
        runBlocking {
            transactions.forEach {
                transactionRepository.save(it)
            }

            val response = webTestClient.get()
                .uri("/rates/transactions-history?userId=111")
                .exchange()
                .expectStatus().isOk
                .expectBodyList(Transaction::class.java)
                .returnResult()
                .responseBody!!

            Assertions.assertEquals(response.size, transactions.size)
            transactionRepository.deleteAll()
        }
    }

    @Test
    fun `GIVEN invalid params, WHEN getTransactionHistory, THEN returns BadRequestException`() {
        webTestClient.get()
            .uri("/rates/transactions-history?userId=")
            .exchange()
            .expectStatus().isBadRequest
    }

    private fun getResourceAsString(pathName: String): String {
        return javaClass.getResource(pathName).readText()
    }

}