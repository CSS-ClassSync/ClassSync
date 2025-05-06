plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

javafx {
    version = "21"
    modules("javafx.controls")
}

dependencies {
    implementation("io.github.spair:imgui-java-app:1.89.0")
}

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

application {
    mainClass = "io.github.ClassSyncCSS.ClassSync.UI.ClassSync"
}