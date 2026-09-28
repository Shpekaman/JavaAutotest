
plugins {
    id("java")
    id("io.qameta.allure") version "2.12.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

val aspectjweaver: Configuration by configurations.creating

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.rest-assured:rest-assured:5.5.6")
    testImplementation("io.rest-assured:json-path:5.4.0")
    testImplementation("org.assertj:assertj-core:3.27.7")
    implementation("com.fasterxml.jackson.core:jackson-core:2.20.1")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("org.seleniumhq.selenium:selenium-java:4.47.0")
    implementation("com.codeborne:selenide:7.18.1")

    testImplementation("io.qameta.allure:allure-junit5:2.29.0")
    testImplementation("io.qameta.allure:allure-selenide:2.29.0")
    testImplementation("io.qameta.allure:allure-rest-assured:2.29.0")
    testImplementation("io.qameta.allure:allure-java-commons:2.29.0")
    aspectjweaver("org.aspectj:aspectjweaver:1.9.22.1")
}

allure {
    version.set("2.29.0")
}

//Общая настройка для всех тестовых задач: Allure-results и javaagent для @Step
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    systemProperty("allure.results.directory", layout.buildDirectory.dir("allure-results").get().asFile.absolutePath)
    doFirst {
        jvmArgs("-javaagent:${aspectjweaver.resolve().first()}")
    }
}



tasks.test {
    useJUnitPlatform()
}

//smoke
tasks.register<Test>("smoke") {
        useJUnitPlatform {
        includeTags("smoke")
    }
    description = "smoke"
}


tasks.register<Test>("AllTest") {
    dependsOn(tasks.test)
}
tasks.register<Test>("apiTest") {
    useJUnitPlatform {
        includeTags("apiTest")
    }
            description = "API"
}

tasks.register("notificationTask") {
    doLast {
        println("Test run is over!")
    }
}

tasks.test {
    finalizedBy("notificationTask")
}

tasks.named<Test>("smoke") {
    finalizedBy("notificationTask")
}

tasks.named<Test>("AllTest") {
    finalizedBy("notificationTask")
}

