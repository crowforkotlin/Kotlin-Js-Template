pluginManagement {
    repositories {
        // maven { setUrl("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "Kotlin-Js-Zipline"

include(":app")
include(":zipline")
