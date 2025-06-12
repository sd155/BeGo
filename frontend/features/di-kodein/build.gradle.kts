plugins {
    alias(libs.plugins.kotlinMultiplatform)
}
private val _java = libs.versions.java.get()

kotlin {
    jvmToolchain(_java.toInt())
    jvm()

    sourceSets {
        commonMain.dependencies {
            api(libs.kodein.di)
        }
    }
}
