plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

group = "com.mordecai.js"
version = "1.0.0"

kotlin {
    js {
        outputModuleName = "Kotlin-Js"
        useCommonJs()
        nodejs()
        browser {
            commonWebpackConfig {
                outputFileName = "kotlin.js"
                cssSupport { enabled.set(true) }
            }
        }
        binaries.executable()
    }
    dependencies {  }
    sourceSets {
        commonMain {
            dependencies {
            }
        }
    }
}