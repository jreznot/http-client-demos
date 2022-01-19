package com.jetbrains.tbe.server.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration(proxyBeanMethods = false)
class ResourceServerConfiguration {
  @Bean
  fun configure(
    http: ServerHttpSecurity,
    authenticationConfig: AuthenticationConfig,
    objectMapper: ObjectMapper
  ): SecurityWebFilterChain {
    // @formatter:off

    return http
      .logout().disable()
      .httpBasic().disable()
      .formLogin().disable()
      .cors().and()
      .csrf()
        .requireCsrfProtectionMatcher(
          notMatch(exchangePathMatch("/api/**"))
        ).and()
      .authorizeExchange()
        .pathMatchers("/api/ping").permitAll()

        .pathMatchers("/api/users/me").hasRole(ROLE_USER)
        .pathMatchers("/api/visits/**").hasRole(ROLE_ADMIN)
      .anyExchange().denyAll()
      .and()
      .enableOAuthProvider(authenticationConfig, objectMapper)
      .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // stateless
      .requestCache()
        .requestCache(NoOpServerRequestCache.getInstance()).and()
      .build()

    // @formatter:on
  }

  @Bean
  fun noopReactiveAuthenticationManager(): ReactiveAuthenticationManager {
    return ReactiveAuthenticationManager { Mono.empty() } // Disable default user/password authentication
  }

  fun ServerHttpSecurity.enableOAuthProvider(
    authenticationConfig: AuthenticationConfig,
    objectMapper: ObjectMapper
  ): ServerHttpSecurity {
    val oauth2ResourceServer = this.oauth2ResourceServer()
    oauth2ResourceServer.authenticationEntryPoint(AuthenticationErrorsEntryPoint(objectMapper))

    val decoder = NimbusReactiveJwtDecoder.withJwkSetUri(authenticationConfig.jwtCertsUrl)
      .build()

    val authenticationManager = JwtReactiveAuthenticationManager(decoder).apply {
      setJwtAuthenticationConverter(JwtAuthenticationProcessor(authenticationConfig))
    }
    oauth2ResourceServer.authenticationManagerResolver { Mono.just(authenticationManager) }

    return this
  }
}
