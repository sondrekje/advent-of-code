plugins {
    kotlin("jvm") version "2.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.11.3")
    implementation("org.jetbrains.kotlin:kotlin-test:2.1.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "8.11.1"
    }
}
