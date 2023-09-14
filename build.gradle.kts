/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    java
    signing
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    implementation("com.discord4j:discord4j-core:3.3.0-M2")
}

group = "dev.hc224"
version = "1.3.0.s2-3.3.0-M2"
description = "SlashLib"
java.sourceCompatibility = JavaVersion.VERSION_1_8

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
    repositories {
        maven {
            name = "HC224Releases"
            url = uri("https://reposilite.hc224.dev/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
