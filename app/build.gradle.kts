plugins {
    application
    java
    jacoco
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.spair:imgui-java-app:1.89.0")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.mockito:mockito-core:5.12.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

jacoco {
    toolVersion = "0.8.10"
}

tasks.jacocoTestReport {
//    dependsOn(tasks.test)
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco"))
    }
    executionData.from(fileTree(layout.buildDirectory).include("/jacoco/*.exec"))

    onlyIf {
        executionData.files.any { it.exists() }
    }
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
    // Generate test results even if tests fail
    ignoreFailures = true

    // Still finalize with JaCoCo report
    finalizedBy(tasks.jacocoTestReport)
}


java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

application {
    mainClass = "io.github.ClassSyncCSS.ClassSync.UI.ClassSync"


}

