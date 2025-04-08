@file:OptIn(ExperimentalDistributionDsl::class)

import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl

// breakpoint-ui-server/build.gradle.kts
plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.10"
    id("org.jetbrains.compose") version "1.7.3"
    kotlin("plugin.serialization") version "2.1.10"
}

repositories {
    google()
    mavenCentral()
}

kotlin {
    js(IR) {
        browser {
            binaries.executable()
            webpackTask {
                outputDirectory = file("$projectDir/build/distributions/assets/js")
            }
        }
    }

    jvm()
    jvmToolchain(17)

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
        }
        jsMain.dependencies {
            implementation(compose.html.core)
        }
        jvmMain.dependencies {
            implementation("io.ktor:ktor-server-websockets:3.1.2")
            implementation("io.ktor:ktor-server-core:3.1.2")
            implementation("io.ktor:ktor-server-netty:3.1.2")
        }
    }
}

tasks.withType<ProcessResources> {
    exclude("**/index.html")
}

tasks.register<Copy>("copyIndexHtml") {
    from("src/jsMain/resources/index.html")
    into("build/distributions/assets/js/")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.named("jsBrowserDistribution") {
    finalizedBy("copyIndexHtml") // ensures copy runs after js build
}


tasks.named("build") {
    dependsOn("jsBrowserProductionWebpack")
}

