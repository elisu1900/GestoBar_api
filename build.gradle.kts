plugins {
	java
	id("org.springframework.boot") version "3.5.13"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.elias"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {


	// ── Core Web ──────────────────────────────────────────
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// ── Persistencia ──────────────────────────────────────
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")

	// ── Seguridad ─────────────────────────────────────────
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.auth0:java-jwt:4.4.0")

	// ── Kotlin ────────────────────────────────────────────
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// ── Caché (pedidos/sesiones de caja) ──────────────────
	implementation("org.springframework.boot:spring-boot-starter-cache")

	// ── Eventos en tiempo real (pantalla cocina/barra) ─────
	implementation("org.springframework.boot:spring-boot-starter-websocket")

	// ── Documentación API ─────────────────────────────────
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

	// ── Utilidades ────────────────────────────────────────
	implementation("org.mapstruct:mapstruct:1.6.3")
	implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
