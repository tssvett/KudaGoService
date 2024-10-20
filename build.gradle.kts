plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    application
}

group = "dev.tssvett"
version = "1.0-SNAPSHOT"
val ktor_version = "3.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))


    testImplementation("io.ktor:ktor-client-mock:2.0.0") // Для мокирования Ktor клиента

    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.mockk:mockk:1.13.13")
    testImplementation("io.ktor:ktor-client-mock:3.0.0")
    testImplementation("io.ktor:ktor-client-content-negotiation:3.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

    dependencies {
        implementation("io.ktor:ktor-client-core:$ktor_version")
        implementation("io.ktor:ktor-client-cio:$ktor_version")
        implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
        implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
        implementation("org.slf4j:slf4j-api:1.7.32")
        // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
        testImplementation("ch.qos.logback:logback-classic:1.5.11")

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