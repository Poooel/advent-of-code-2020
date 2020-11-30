plugins {
    kotlin("jvm") version "1.4.10"
    application
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:30.0-jre")
    implementation("com.github.ajalt.clikt:clikt:3.0.1")
    implementation("com.github.ajalt.mordant:mordant:2.0.0-alpha1")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.3")
    implementation("khttp:khttp:1.0.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("com.github.advent.of.code.AppKt")
}
