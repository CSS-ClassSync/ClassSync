plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.spair:imgui-java-app:1.89.0")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.mockito:mockito-core:5.12.0")
}

tasks.test {
    useJUnitPlatform()
}


java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

application {
    mainClass = "io.github.ClassSyncCSS.ClassSync.UI.ClassSync"
}

