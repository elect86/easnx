import org.example.Arch
import org.example.OS
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)

    alias(libs.plugins.jetbrainsCompose)

    id("native-support")
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
            implementation("com.zoffcc.applications:jni_notifications:0.0.3")
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

//configurations.configureEach {
//    //    println("[$this] $attributes")
//    if (name.endsWith("CompileClasspath") || name.endsWith("RuntimeClasspath"))
//        attributes.getAttribute(Usage.USAGE_ATTRIBUTE)?.name?.let { usage ->
//            if (usage == Usage.JAVA_API || usage == Usage.JAVA_RUNTIME)
//                attributes {
//                    // select a platform, will fail to compose a runtime classpath if none is selected
//                    attribute(Attribute.of(OS::class.java), OS.linux) // or MACOS or LINUX
//                    attribute(Attribute.of(Arch::class.java), Arch.x86_64)   // or x86-64 or arm32 or arm64
//                }
//        }
//}
//configurations.configureEach {
////    println(this.name)
//    attributes {
//        if(getAttribute(Usage.USAGE_ATTRIBUTE)?.name == Usage.JAVA_RUNTIME
//            && getAttribute(Category.CATEGORY_ATTRIBUTE)?.name == Category.LIBRARY
////            && getAttribute(Bundling.BUNDLING_ATTRIBUTE)?.name == Bundling.EXTERNAL
//            && getAttribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE)?.name == LibraryElements.JAR)
//            println("found ${this@configureEach} $attributes")
////        attribute(Attribute.of(OS::class.java), variantDefinition.os)
////        attribute(Attribute.of(Arch::class.java), variantDefinition.arch)
//    }
//}