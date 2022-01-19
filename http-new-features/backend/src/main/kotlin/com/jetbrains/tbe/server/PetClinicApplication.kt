package com.jetbrains.tbe.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class PetClinicApplication

fun main(args: Array<String>) {
  runApplication<PetClinicApplication>(*args)
}
