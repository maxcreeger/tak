plugins {
    kotlin("jvm") version "2.1.20"
    idea
}

group = "org.marmotte.tak"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.14.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}