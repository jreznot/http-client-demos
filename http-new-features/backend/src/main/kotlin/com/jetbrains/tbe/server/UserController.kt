package com.jetbrains.tbe.server

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController {
  @GetMapping("/me")
  fun getMe(@AuthenticationPrincipal(expression = "") principal: Jwt): UserMe {
    return UserMe(principal.getClaimAsString("preferred_username"))
  }

  class UserMe(
    val name: String
  )
}
