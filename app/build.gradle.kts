plugins {
    application
    java
}

group = "com.rental"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    // Gson untuk JSON
    implementation("com.google.code.gson:gson:2.13.1")

    // Guava (jika masih digunakan)
    implementation(libs.guava)

    // JUnit untuk testing
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass.set("com.rental.Main")
}

tasks.test {
    useJUnitPlatform()
}