package com.jetbrains.tbe.server.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.DisabledException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.server.resource.web.server.BearerTokenServerAuthenticationEntryPoint
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.util.MimeTypeUtils
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

class AuthenticationErrorsEntryPoint(
  private val objectMapper: ObjectMapper
) : ServerAuthenticationEntryPoint {
  private val delegate: ServerAuthenticationEntryPoint = BearerTokenServerAuthenticationEntryPoint()

  override fun commence(exchange: ServerWebExchange, ex: AuthenticationException): Mono<Void> {
    if (ex is DisabledException) {
      return replyWithMessage(exchange, HttpStatus.FORBIDDEN, "User account is deactivated")
    }
    if (ex is UsernameNotFoundException) {
      return replyWithMessage(exchange, HttpStatus.UNAUTHORIZED, "User account not found")
    }
    return delegate.commence(exchange, ex)
  }

  private fun replyWithMessage(
    exchange: ServerWebExchange,
    statusCode: HttpStatus,
    message: String?
  ): Mono<Void> {
    return Mono.defer {
      val response = exchange.response
      response.headers[HttpHeaders.WWW_AUTHENTICATE] = "Bearer"
      response.headers[HttpHeaders.CONTENT_TYPE] = MimeTypeUtils.APPLICATION_JSON_VALUE
      response.statusCode = statusCode
      response.writeWith(Mono.just(createErrorData(exchange, message)))
        .then(Mono.defer { response.setComplete() })
    }
  }

  private fun createErrorData(exchange: ServerWebExchange, message: String?): DataBuffer {
    val errorData = objectMapper.createObjectNode()
    errorData.put("message", message ?: "Unauthorized")
    val jsonData = objectMapper.writeValueAsString(errorData)
      .toByteArray(StandardCharsets.UTF_8)
    return exchange.response.bufferFactory().wrap(jsonData)
  }
}
