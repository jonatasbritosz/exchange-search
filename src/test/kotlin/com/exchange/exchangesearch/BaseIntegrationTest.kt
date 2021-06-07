package com.exchange.exchangesearch

import org.junit.jupiter.api.TestInstance
import org.springframework.context.annotation.Import

@Import(IntegrationTestConfig::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseIntegrationTest : BaseTest()