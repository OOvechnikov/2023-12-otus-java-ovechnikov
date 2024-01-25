plugins {
    id("com.github.johnrengelman.shadow")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    compileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core")
}
