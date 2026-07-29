plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("io.rest-assured:json-path:5.4.0")


    implementation("com.fasterxml.jackson.core:jackson-core:2.20.1")
}

//Задача №1 создать две задачи одна запускает все тесты, вторая выводит Test run is over и запскается после завершения первой
tasks.test {
    useJUnitPlatform()
}

tasks.register("runAllTest") {
    group = "verification"
    dependsOn("clean","test")
    doLast {
        println("Test run is Over")
    }
}

