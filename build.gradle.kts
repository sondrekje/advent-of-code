plugins {
    kotlin("jvm") version "1.7.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
    implementation("org.jetbrains.kotlin:kotlin-test:1.7.21")
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
        gradleVersion = "7.3"
    }
}
