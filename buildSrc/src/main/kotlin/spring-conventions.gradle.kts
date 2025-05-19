plugins {
    id("kotlin-conventions")
    id("org.springframework.boot")
    id("org.jetbrains.kotlin.plugin.spring")
    id("io.spring.dependency-management")
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.springframework.boot:spring-boot-starter-validation")
}
