plugins {
	`spring-conventions`
	`kotlin-conventions`
	`mongodb-conventions`
}

dependencies {
	implementation(libs.spring.kafka)
	implementation(libs.reactor.kafka)
	implementation(libs.faker)
}

repositories {
	mavenCentral()
	maven {
		setUrl("https://packages.confluent.io/maven/")
	}
}
