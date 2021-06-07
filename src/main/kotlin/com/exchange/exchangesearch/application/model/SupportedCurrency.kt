package com.exchange.exchangesearch.application.model

enum class SupportedCurrency {
    BRL, USD, EUR, JPY, GBP;

    companion object{
        fun getSupportedCurrencies() = values().map { it.toString() }.toList()
    }
}