package com.jetbrains.tbe.server.security

import org.intellij.lang.annotations.Language
import org.springframework.http.HttpMethod
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher

fun notMatch(matcher: ServerWebExchangeMatcher): ServerWebExchangeMatcher {
  return NegatedServerWebExchangeMatcher(matcher)
}

fun exchangePathMatch(
  @Language("http-url-reference") pattern: String,
  method: HttpMethod? = null
): ServerWebExchangeMatcher {
  if (method != null) {
    return PathPatternParserServerWebExchangeMatcher(pattern, method)
  }
  return PathPatternParserServerWebExchangeMatcher(pattern)
}
