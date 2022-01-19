plugins {
  kotlin("jvm") version "1.6.10" apply false
  kotlin("kapt") version "1.6.10" apply false
  kotlin("plugin.spring") version "1.6.10" apply false
  id("org.springframework.boot") version "2.6.1" apply false
  id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false

  `java-library`
}

group = "org.jetbrains.pets"
version = System.getenv("BUILD_NUMBER") ?: "0.0.1-SNAPSHOT"

subprojects {
  repositories {
    mavenCentral()
  }

  apply(plugin = "java-library")

  group = rootProject.group
  version = rootProject.version

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
      jvmTarget = "11"
    }
  }

  extensions.configure(JavaPluginExtension::class) {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

tasks.wrapper {
  distributionType = Wrapper.DistributionType.BIN
}
