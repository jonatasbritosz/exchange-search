package com.exchange.exchangesearch.application.configuration

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
class R2DBCConfiguration {

    @Bean
    fun databaseConnectionInitializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        val connectionFactoryInitializer = ConnectionFactoryInitializer()
        connectionFactoryInitializer.setConnectionFactory(connectionFactory);
        connectionFactoryInitializer.setDatabasePopulator(ResourceDatabasePopulator(ClassPathResource("schema.sql")));
        return connectionFactoryInitializer
    }
}