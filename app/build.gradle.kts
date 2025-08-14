import app.cash.zipline.gradle.ZiplineServeTask
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.zipline)
    alias(libs.plugins.android.library)
}

group = "com.mordecai"
version = "1.0.0"

kotlin {
    androidTarget()
    js {
        browser()
        binaries.executable()
    }
    dependencies {  }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.zipline)
            }
        }
    }
}

android {
    compileSdk = 36
    namespace = "com.mordecai.zipline.presenter"
    defaultConfig { minSdk = 23 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

zipline {
    mainFunction.set("com.mordecai.zipline.launchZipline")
}
tasks.named<ZiplineServeTask>("serveDevelopmentZipline") {
    port
    inputDir
}

plugins.withType<YarnPlugin> {
    the<YarnRootExtension>().yarnLockAutoReplace = true
}
