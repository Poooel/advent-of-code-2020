plugins {
    kotlin("jvm") version "1.5.31"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("com.github.ajalt.clikt:clikt:3.3.0")
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta3")
    implementation("com.github.kittinunf.fuel:fuel:2.3.1")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("com.github.advent.of.code.AppKt")
}
