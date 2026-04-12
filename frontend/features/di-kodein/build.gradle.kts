plugins {
    alias(libs.plugins.kotlinMultiplatform)
}
private val _java = libs.versions.java.get()

kotlin {
    jvmToolchain(_java.toInt())
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kodein.di)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.test.junit)
        }
    }
}
