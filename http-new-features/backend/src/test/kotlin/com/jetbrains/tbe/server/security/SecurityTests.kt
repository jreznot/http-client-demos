package com.jetbrains.tbe.server.security

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@ActiveProfiles("dev", "test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SecurityTests {
  lateinit var client: WebTestClient

  @BeforeEach
  fun setUp(context: ApplicationContext) {
    client = WebTestClient.bindToApplicationContext(context).build()
  }

  @Test
  fun `ping responds`() {
    client.get()
      .uri("/api/ping")
      .exchange()
      .expectBody()
      .jsonPath("$.message").isEqualTo("pong")
  }
}
