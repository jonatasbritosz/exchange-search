package com.exchange.exchangesearch.application.service

import com.exchange.exchangesearch.application.exception.BadRequestException
import com.exchange.exchangesearch.application.exception.ForbiddenException
import com.exchange.exchangesearch.application.exception.NotFoundException
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.util.Loggers

abstract class BaseService {

    private val logger = Loggers.getLogger(BaseService::class.java)

    protected fun handleClientException(ex: WebClientResponseException): Exception {
        logger.warn("handleClientException: error requesting exchangeratesapi.io, response=${ex.responseBodyAsString}")
        return when (ex) {
            is WebClientResponseException.BadRequest -> BadRequestException(ex.localizedMessage)
            is WebClientResponseException.Forbidden -> ForbiddenException(ex.localizedMessage)
            is WebClientResponseException.NotFound -> NotFoundException(ex.localizedMessage)
            else -> ex
        }
    }

    protected fun handleUnexpectedException(ex: Exception): Exception {
        logger.error("handleUnexpectedException: error processing request message=${ex.message}")
        return ex
    }
}