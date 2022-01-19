package com.jetbrains.tbe.server.ping

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ping")
class PingController(
  private val objectMapper: ObjectMapper
) {
  @GetMapping
  fun ping(): ResponseEntity<JsonNode> {
    val data = objectMapper.createObjectNode().apply {
      put("message", "pong")
    }
    return ResponseEntity.ok().body(data)
  }
}
