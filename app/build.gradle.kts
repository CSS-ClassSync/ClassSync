plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.spair:imgui-java-app:1.89.0")
}

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

application {
    mainClass = "io.github.ClassSyncCSS.ClassSync.UI.ClassSync"
}