package com.exchange.exchangesearch

import com.exchange.exchangesearch.infrastructure.outgoing.adapters.ApiExchangeAdapter
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class IntegrationTestConfig {

    companion object {
        private const val MOCKWEBSERVER_HOST = "http://localhost:"
    }

    @Bean
    fun mockWebServer() = MockWebServer().apply { start() }

    @Bean(name = ["apiExchangeAdapterMock"])
    @Primary
    fun apiExchangeAdapter(mockWebServer: MockWebServer): ApiExchangeAdapter {
        return ApiExchangeAdapter(baseUrl = MOCKWEBSERVER_HOST.plus(mockWebServer.port))
    }

    @AfterAll
    fun tearDown(mockWebServer: MockWebServer) {
        mockWebServer.shutdown()
    }
}