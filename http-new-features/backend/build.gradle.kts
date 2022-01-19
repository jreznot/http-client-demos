plugins {
  id("org.springframework.boot")
  id("io.spring.dependency-management")
  kotlin("jvm")
  kotlin("kapt")
  kotlin("plugin.spring")
}

extra["springCloudVersion"] = "2021.0.0"
if (rootProject.hasProperty("overrideVersion")) {
  version = rootProject.properties["overrideVersion"] as String
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.security:spring-security-oauth2-resource-server")
  implementation("org.springframework.security:spring-security-oauth2-jose")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.hibernate.validator:hibernate-validator")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.security:spring-security-test")

  // generate metadata for custom config POJOs, enable code-insight in YAML for them
  kapt("org.springframework.boot:spring-boot-configuration-processor")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}

kapt {
  includeCompileClasspath = false // only create Spring configuration JSON from project classes
}

sourceSets.test { // enable YAML code-insight for custom properties in test sources
  resources.srcDir(File(projectDir, "build/tmp/kapt3/classes/main"))
}
