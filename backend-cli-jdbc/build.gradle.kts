plugins {
    id("java")
    id("org.sonarqube") version "6.2.0.5505"
}
sonar {
    properties {
        property("sonar.projectKey", "Runoler_oit-practice")
        property("sonar.organization", "runoler")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.postgresql:postgresql:42.7.7")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}