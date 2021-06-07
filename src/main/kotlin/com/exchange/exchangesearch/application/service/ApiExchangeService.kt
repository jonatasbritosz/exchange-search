package com.exchange.exchangesearch.application.service

import com.exchange.exchangesearch.application.exception.BadRequestException
import com.exchange.exchangesearch.application.model.SupportedCurrency
import com.exchange.exchangesearch.infrastructure.outgoing.adapters.ApiExchangeAdapter
import com.exchange.exchangesearch.infrastructure.outgoing.model.ExchangeResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClientResponseException

@Component
class ApiExchangeService(
    @Value("\${exchange.api.access-key}") private val accessKey: String,
    private val apiExchangeAdapter: ApiExchangeAdapter
) : BaseService() {

    companion object {
        const val PARAM_ACCESS_KEY = "access_key"
        const val PARAM_BASE_CURRENCY = "base"
        const val PARAM_SYMBOLS = "symbols"
    }

    suspend fun searchExchangeRates(
        userId: Long,
        originCurrency: String,
        destinationCurrency: String
    ): ExchangeResponse {

        return try {
            var queryParams: MultiValueMap<String, String> = LinkedMultiValueMap()
            queryParams = validateParams(originCurrency, destinationCurrency, queryParams)

            queryParams.add(PARAM_ACCESS_KEY, accessKey)
            apiExchangeAdapter.searchExchangeRates(queryParams)

        } catch (ex: WebClientResponseException) {
            if (ex.statusCode.is4xxClientError) {
                throw handleClientException(ex)
            } else {
                throw handleUnexpectedException(ex)
            }
        } catch (ex: Exception) {
            throw handleUnexpectedException(ex)
        }
    }

    fun validateParams(
        originCurrency: String,
        destinationCurrency: String,
        queryParams: MultiValueMap<String, String>
    ): MultiValueMap<String, String> {
        val supportedCurrencies = SupportedCurrency.getSupportedCurrencies()

        if (originCurrency.isBlank()) {
            throw BadRequestException(message = "Required String parameter 'destinationCurrency' was blank!")
        } else {
            if (supportedCurrencies.contains(originCurrency)) {
                queryParams.add(PARAM_BASE_CURRENCY, originCurrency)
            } else {
                throw BadRequestException(message = "Origin currency is not supported! These are the supported values: $supportedCurrencies")
            }
        }
        if (destinationCurrency.isBlank()) {
            throw BadRequestException(message = "Required String parameter 'destinationCurrency' was blank!")
        } else {
            if (supportedCurrencies.contains(destinationCurrency)) {
                queryParams.add(PARAM_SYMBOLS, destinationCurrency)
            } else {
                throw BadRequestException(message = "Destination currency is not supported! These are the supported values: $supportedCurrencies")
            }
        }
        return queryParams
    }
}