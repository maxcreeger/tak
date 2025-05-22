plugins {
    kotlin("jvm") version "2.1.20"
    idea
}

group = "org.marmotte.tak"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}