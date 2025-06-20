import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

private val _java = libs.versions.java.get()
private val _javaVersion = JavaVersion.toVersion(_java)
private val _jvmTarget = JvmTarget.fromTarget(_java)

kotlin {
    jvmToolchain(_java.toInt())

    androidTarget {
        compilerOptions {
            jvmTarget.set(_jvmTarget)
        }
    }
    
    sourceSets {        
        commonMain.dependencies {
            implementation(projects.features.diKodein)
            implementation(projects.features.result)
            implementation(projects.features.theme)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.compose.android.lifecycle.viewmodel)
            implementation(libs.compose.viewmodel)
            implementation(libs.sd155.kmplogs.api)
            implementation(compose.components.resources)
        }
        androidMain.dependencies {
            implementation(libs.google.gms.location)
        }
    }
}

android {
    namespace = "${libs.versions.application.namespace.get()}.tracker"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "../proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = _javaVersion
        targetCompatibility = _javaVersion
    }
    dependencies {
        debugImplementation(libs.androidx.ui.tooling)
    }
} 