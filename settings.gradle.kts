rootProject.name = "2023-12-otus-java-ovechnikov"
include("hw01-gradle")
include("hw02")
include("hw03")
include("hw03-framework")
include("hw04-gc")
include("hw05-aop")
include("hw06-atm")
include("hw07-patterns")
include("hw08-io")
include("hw09-jdbc:demo")
include("hw09-jdbc:homework")
include("hw10-jpql")
include("hw11-cache")
include("hw12-webServer")

pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings
    val protobufVer: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("io.spring.dependency-management") version dependencyManagement

        id("org.springframework.boot") version springframeworkBoot
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("com.google.cloud.tools.jib") version jib
        id("com.google.protobuf") version protobufVer
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }

}
include("hw12-webServer")
