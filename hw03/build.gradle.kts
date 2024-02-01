plugins {
    id("com.github.johnrengelman.shadow")
}

dependencies {
    testImplementation(project(":hw03-framework"))
    testImplementation("org.assertj:assertj-core")
}
