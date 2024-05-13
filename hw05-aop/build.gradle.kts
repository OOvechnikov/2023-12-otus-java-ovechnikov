plugins {
    id("com.github.johnrengelman.shadow")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation ("org.reflections:reflections:0.10.2")
    implementation ("ch.qos.logback:logback-classic")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}
