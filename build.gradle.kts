
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
    implementation("io.rest-assured:rest-assured:5.5.6")
    testImplementation("io.rest-assured:json-path:5.4.0")
    // Source: https://mvnrepository.com/artifact/org.assertj/assertj-core
    testImplementation("org.assertj:assertj-core:3.27.7")
    implementation("com.fasterxml.jackson.core:jackson-core:2.20.1")

}

//«адача є1 создать две задачи одна запускает все тесты, втора€ выводит Test run is over и запускаетс€ после завершени€ первой

tasks.test {
    useJUnitPlatform()
}

//только smoke
tasks.register<Test>("smoke") {
        useJUnitPlatform {
        includeTags("smoke")
    }
    description = "«апуск тестового задани€ с тегом smoke"
}

//все тесты
tasks.register<Test>("AllTest") {
    dependsOn(tasks.test)
}
//выводит Test run is over!
tasks.register("notificationTask") {
    doLast {
        println("Test run is over!")
    }
}
//ѕрив€зывает notificationTask к задачам
tasks.test {
    finalizedBy("notificationTask")
}

tasks.named<Test>("smoke") {
    finalizedBy("notificationTask")
}

tasks.named<Test>("AllTest") {
    finalizedBy("notificationTask")
}

