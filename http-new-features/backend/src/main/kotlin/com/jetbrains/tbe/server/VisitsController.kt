package com.jetbrains.tbe.server

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.OffsetDateTime

@RestController
@RequestMapping("/api/visits")
class VisitsController {
  @GetMapping(produces = ["application/stream+json"])
  fun getAll(): Flux<VisitData> {
    return Flux.generate<VisitData?> {
      val ts = OffsetDateTime.now()
      it.next(VisitData(ts))
    }.delayElements(Duration.ofSeconds(1))
  }

  @GetMapping(value = ["/qr.svg"], produces = ["application/svg+xml"])
  fun svgQrCode(): Flux<DataBuffer> {
    return DataBufferUtils.read(ClassPathResource("/qr.svg"), DefaultDataBufferFactory.sharedInstance, 1024)
  }

  @GetMapping(value = ["/qr.png"], produces = ["image/png"])
  fun pngQrCode(): Flux<DataBuffer> {
    return DataBufferUtils.read(ClassPathResource("/qr.png"), DefaultDataBufferFactory.sharedInstance, 1024)
  }

  @GetMapping("/download")
  fun download(): ResponseEntity<Any> {
    return ResponseEntity
      .status(HttpStatus.FOUND)
      .header("Location", "http://resources.jetbrains.com/help/img/idea/2021.3/basic_request.animated.gif")
      .build()
  }

  class VisitData(
    val ts: OffsetDateTime
  )
}
