plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    application
}

group = "dev.tssvett"
version = "1.0-SNAPSHOT"
val ktor_version: String by project

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.12.0")
    dependencies {
        implementation("io.ktor:ktor-client-core:$ktor_version")
        implementation("io.ktor:ktor-client-cio:$ktor_version")
        implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
        implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
        implementation("org.slf4j:slf4j-api:1.7.32")
        implementation("ch.qos.logback:logback-classic:1.2.6")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}