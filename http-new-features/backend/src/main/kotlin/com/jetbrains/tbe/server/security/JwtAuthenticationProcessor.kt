package com.jetbrains.tbe.server.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import reactor.core.publisher.Mono

class JwtAuthenticationProcessor(
  private val authenticationConfig: AuthenticationConfig
) : Converter<Jwt, Mono<AbstractAuthenticationToken>> {

  private val authoritiesConverter: Converter<Jwt, Collection<GrantedAuthority>> = JwtGrantedAuthoritiesConverter()

  override fun convert(token: Jwt): Mono<AbstractAuthenticationToken> {
    return Mono.fromCallable { convertToken(token) }
  }

  private fun convertToken(token: Jwt): AbstractAuthenticationToken {
    val authorities = authoritiesConverter.convert(token)?.toMutableList() ?: mutableListOf()
    val email: String? = token.getClaimAsString("email")

    return if (email != null) {
      authorities.add(SimpleGrantedAuthority(ROLE_PREFIX + ROLE_USER))
      if (authenticationConfig.isConfiguredAdmin(email)) {
        authorities.add(SimpleGrantedAuthority(ROLE_PREFIX + ROLE_ADMIN))
      }
      JwtAuthenticationToken(token, authorities)
    } else {
      JwtAuthenticationToken(token, authorities)
    }
  }
}
