package com.jetbrains.tbe.server.security

import org.hibernate.validator.constraints.URL
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "petclinic.auth")
@ConstructorBinding
class AuthenticationConfig(
  @field:URL @Suppress("unused")
  val jwtCertsUrl: String?,
  private val rootAdminEmails: List<String> = listOf()
) {
  fun isConfiguredAdmin(email: String?): Boolean {
    return getConfiguredAdminEmails().any { it.equals(email, ignoreCase = true) }
  }

  fun getConfiguredAdminEmails(): Set<String> {
    return rootAdminEmails.toSortedSet()
  }
}
