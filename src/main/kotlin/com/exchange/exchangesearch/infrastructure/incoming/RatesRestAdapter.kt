package com.exchange.exchangesearch.infrastructure.incoming

import com.exchange.exchangesearch.application.exception.ExceptionResponse
import com.exchange.exchangesearch.domain.ApiExchangeDomainService
import com.exchange.exchangesearch.infrastructure.outgoing.model.Transaction
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rates")
class RatesRestAdapter @Autowired constructor(
    private val apiExchangeDomainService: ApiExchangeDomainService
) {

    @Operation(summary = "Get rates for certain currencies among those allowed: [BRL, USD, EUR, JPY, GBP]")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Rates Found",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = Transaction::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ExceptionResponse::class)
                    )
                ]
            )
        ]
    )
    @GetMapping
    suspend fun getRates(
        @Parameter(description = "userId making the request")
        @RequestParam userId: Long,
        @Parameter(description = "Source currency to convert value")
        @RequestParam originCurrency: String,
        @Parameter(description = "Target currency to convert value")
        @RequestParam destinationCurrency: String,
        @Parameter(description = "Value to conversion")
        @RequestParam value: Double,
    ) = apiExchangeDomainService.getRates(userId, originCurrency, destinationCurrency, value)


    @Operation(summary = "Get transaction history in list format for a particular user")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Transactions Found",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = Transaction::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ExceptionResponse::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("/transactions-history")
    suspend fun getTransactionHistory(
        @Parameter(description = "userId who made the requests")
        @RequestParam userId: Long,
        @Parameter(description = "sort direction for list result")
        @RequestParam(required = false, defaultValue = "ASC") sort: Sort.Direction
    ) = apiExchangeDomainService.getTransactionHistory(userId, sort)

}