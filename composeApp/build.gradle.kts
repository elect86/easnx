import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)

    alias(libs.plugins.jetbrainsCompose)
    id("com.google.osdetector") version "1.7.3"
}

repositories {
    mavenCentral()
    google()
    maven("../../jni_notifications/repo")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation("commons-codec:commons-codec:1.10")
            implementation("com.zoffcc.applications:jni_notifications:0.0.2")
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.hzdr"
            packageVersion = "1.0.0"
        }
    }
}

configurations.commonMainRuntimeOnly {
    attributes {
        // select a platform, will fail to compose a runtime classpath if non is selected
        attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, objects.named(OperatingSystemFamily.LINUX)) // or MACOS or LINUX
        attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, objects.named("x86-64"))   // or x86-64 or arm32 or arm64
    }
}
configurations["desktopRuntimeClasspath"].apply {
    this.dependencies.forEach { println("[dependencies] $it") }
    this.allArtifacts.forEach { println("[allArtifacts] $it") }
    this.allDependencies.forEach { println("[allDependencies] $it") }
    println("isCanBeResolved $isCanBeResolved, isCanBeConsumed $isCanBeConsumed")
}
configurations["desktopRuntimeClasspath"].attributes {
    // select a platform, will fail to compose a runtime classpath if non is selected
    attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, objects.named(OperatingSystemFamily.LINUX)) // or MACOS or LINUX
    attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, objects.named("x86-64"))   // or x86-64 or arm32 or arm64
}

task("dependency") {
    doFirst {
        println(configurations["desktopRuntimeClasspath"].files.find { "jni" in it.name })
    }
}