plugins {
	`spring-conventions`
	`kotlin-conventions`
	`mongodb-conventions`
}

dependencies {
	implementation(libs.spring.kafka)
	implementation(libs.reactor.kafka)
	implementation(libs.faker)
	implementation("com.auth0:java-jwt:4.5.0")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	implementation(project(":_internal-api"))
}

allprojects {
	group = "pm.gh.integration"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
		mavenLocal()
		gradlePluginPortal()
		maven {
			setUrl("https://packages.confluent.io/maven/")
		}
	}
}
