import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

val keystorePropsFile = rootProject.file("keystore.properties")
val keystoreProps = Properties()
if (keystorePropsFile.exists()) {
    FileInputStream(keystorePropsFile).use { keystoreProps.load(it) }
}

val storeFilePath = keystoreProps.getProperty("storeFile")
val storePassword = keystoreProps.getProperty("storePassword")
val keyAlias = keystoreProps.getProperty("keyAlias")
val keyPassword = keystoreProps.getProperty("keyPassword")
val hasSigning = !storeFilePath.isNullOrBlank()
    && !storePassword.isNullOrBlank()
    && !keyAlias.isNullOrBlank()
    && !keyPassword.isNullOrBlank()

android {
    namespace = "by.instruction.profsouz"
    compileSdk {
        version = release(36)
    }

    if (hasSigning) {
        signingConfigs {
            create("release") {
                storeFile = file(storeFilePath)
                storePassword = storePassword
                keyAlias = keyAlias
                keyPassword = keyPassword
            }
        }
    }

    defaultConfig {
        applicationId = "by.instruction.profsouz"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            if (hasSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.recyclerview)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.room.runtime)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.work.runtime)
    implementation(libs.jsoup)
    implementation(libs.glide)
    annotationProcessor(libs.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}