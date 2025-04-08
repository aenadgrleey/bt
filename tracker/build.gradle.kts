plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.aenadgrleey.bt"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2024.1.7")
    type.set("IU") // Target IDE Platform
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("241")
        untilBuild.set("243.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

dependencies {
    implementation(project(":ui"))
}

tasks.register<Copy>("copyFrontendToResources") {
    from("build/distributions/assets/js")
    into("src/main/resources/web-assets") // ты можешь выбрать любое имя
}

tasks.named("processResources") {
    dependsOn("copyFrontendToResources")
}

tasks {
    prepareSandbox {
        val frontendOutput = project(":ui").layout.buildDirectory.dir("distributions/assets/js")
        from(frontendOutput) {
            // путь, по которому будет доступно в sandbox
            into(pluginName.map { "$it/frontend-assets" })
        }
    }
}
