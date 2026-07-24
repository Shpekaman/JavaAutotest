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


tasks.test {
    useJUnitPlatform()
    finalizedBy("testIsOver")
}


tasks.register("runAllTest"){
    description="Запускает все тесты"
    group = "verification"
}
tasks.named ("runAllTest"){
    dependsOn("test")
    dependsOn("testIsOver")
}


tasks.register("testIsOver") {
    group = "verification"
    doLast {
    println("Test run is Over")
    }
}

