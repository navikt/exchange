// Common
val kotlinVersion = "1.3.72"
val ktorVersion = "1.3.2"
val kotlinxVersion = "1.3.6"
val konfigVersion = "1.6.10.0"
// Log
val apacheCommonsVersion = "3.10"
val logstashEncoderVersion = "6.3"
val logbackVersion = "1.2.3"
val ioPrometheusVersion = "0.9.0"
val kotlinloggingVersion = "1.7.9"

val mainClassName = "no.nav.exchange.MainKt"

plugins {
    kotlin("jvm") version "1.3.72"
    java
    id("org.jmailen.kotlinter") version "2.3.2"
    id("com.github.ben-manes.versions") version "0.28.0"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://dl.bintray.com/kotlin/ktor")
    maven(url = "https://kotlin.bintray.com/kotlinx")
}

tasks {
    withType<Jar> {
        manifest.attributes["Main-Class"] = mainClassName
    }
    create("printVersion") {
        println(project.version)
    }
    withType<Test> {
        useJUnitPlatform {
            includeEngines("spek")
        }
        testLogging.events("passed", "skipped", "failed")
    }
}

dependencies {
    implementation (kotlin("stdlib"))
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation ("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation ("io.ktor:ktor-client-cio:$ktorVersion")
    implementation ("io.ktor:ktor-server-netty:$ktorVersion")
    implementation ("io.ktor:ktor-client-core:$ktorVersion")
    implementation ("io.ktor:ktor-client-apache:$ktorVersion")
    implementation ("org.apache.commons:commons-lang3:$apacheCommonsVersion")
    implementation ("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVersion")
    implementation ("ch.qos.logback:logback-classic:$logbackVersion")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxVersion")
    implementation ("com.github.kittinunf.fuel:fuel:2.2.3")
    implementation ("io.prometheus:simpleclient_hotspot:$ioPrometheusVersion")
    implementation ("io.prometheus:simpleclient_common:$ioPrometheusVersion")
    implementation ("io.github.microutils:kotlin-logging:$kotlinloggingVersion")
}
