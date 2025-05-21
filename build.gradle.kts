plugins {
	`spring-conventions`
	`kotlin-conventions`
	`mongodb-conventions`
}

dependencies {
	implementation(libs.spring.kafka)
	implementation(libs.reactor.kafka)
	implementation(libs.faker)
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
