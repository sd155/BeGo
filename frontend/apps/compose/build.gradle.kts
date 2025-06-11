import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
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
    
//     jvm("desktop")
    
//     listOf(
//         iosX64(),
//         iosArm64(),
//         iosSimulatorArm64()
//     ).forEach { iosTarget ->
//         iosTarget.binaries.framework {
//             baseName = "composeApp"
//             isStatic = true
//         }
//     }
    
    sourceSets {
        val desktopMain by getting        
        commonMain.dependencies {
//             
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}

android {
    namespace = libs.versions.application.namespace.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.application.namespace.get()
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.application.version.code.get().toInt()
        versionName = libs.versions.application.version.name.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        val appName = libs.versions.application.name.get()
        // getByName("release") {
        //     isMinifyEnabled = true
        //     isShrinkResources = true
        //     proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "../proguard-rules.pro")
        //     signingConfig = signingConfigs.getByName("release")
        //     resValue("String", "app_name", appName)
        // }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            resValue("string", "app_name", "$appName debug")
        }
    }
    compileOptions {
        sourceCompatibility = _javaVersion
        targetCompatibility = _javaVersion
    }
}

// compose.desktop {
//     application {
//         mainClass = "MainKt"
// 
//         nativeDistributions {
//             targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//             packageName = libs.versions.application.namespace.get()
//             packageVersion = libs.versions.application.version.name.get()
//         }
//     }
// }
