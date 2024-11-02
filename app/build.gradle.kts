import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.devtools.ksp)
}

val appSignPassword: String by project

val pathStoreFile = "$rootDir/signing/LivePicturesKeys.jks"

fun getLocalProperty(key: String, file: String = "local.properties"): String {
    val properties = Properties()
    val localProperties = rootProject.file(file)
    if (localProperties.exists()) {
        localProperties.inputStream().use { reader ->
            properties.load(reader)
        }
    }

    return properties.getProperty(key) ?: ""
}

android {
    namespace = "com.dpudov.livepictures"
    compileSdk = Android.compileSdk

    defaultConfig {
        applicationId = Android.applicationId
        minSdk = Android.minSdk
        targetSdk = Android.compileSdk
        versionCode = Android.versionCode
        versionName = Android.versionName

        testInstrumentationRunner = Android.testInstrumentationRunner
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        val password by lazy { getLocalProperty("appSignPassword") }
        getByName("debug") {
            storeFile = file(pathStoreFile)
            storePassword = appSignPassword.ifBlank { password }
            keyAlias = getLocalProperty(
                "keyAlias",
                "$rootDir/signing/keystore-debug.properties"
            )
            keyPassword = appSignPassword.ifBlank { password }
        }

        create("release") {
            storeFile = file(pathStoreFile)
            storePassword = appSignPassword.ifBlank { password }
            keyAlias = getLocalProperty(
                "keyAlias",
                "$rootDir/signing/keystore-release.properties"
            )
            keyPassword = appSignPassword.ifBlank { password }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs["debug"]
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs["release"]
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = Android.jvmTarget
    }

    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.app.domain)
    implementation(projects.app.data)
    implementation(projects.app.datasourceFrames)
    implementation(projects.app.featureGif)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.room.runtime)

    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
