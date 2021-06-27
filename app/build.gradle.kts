plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
    kotlin("android.extensions")
}

android {
    buildToolsVersion = "30.0.3"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        applicationId = "com.skillbox.strava"
        minSdk = rootProject.extra["minSdkVersion"] as Int
        targetSdk = rootProject.extra["compileSdkVersion"] as Int
        versionCode = 1
        versionName = "1.0"
        addManifestPlaceholders( mapOf(Pair("appAuthRedirectScheme", "https://strava/token")))
    }

    buildFeatures.viewBinding = true

    buildTypes {
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    lintOptions {
        isIgnoreTestSources = true
    }
    kapt {
        generateStubs = true
        correctErrorTypes = false
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    androidExtensions {
        isExperimental = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core_network"))
    implementation(project(":shared_model"))
    implementation(project(":core_db"))

    //Dagger-Hilt
    val dagger_version = rootProject.extra["dagger_version"]
    val dagger_viewmodel_version = rootProject.extra["dagger_viewmodel_version"]
    val hilt_work_version = rootProject.extra["hilt_work_version"]
    implementation("com.google.dagger:hilt-android:$dagger_version")
    kapt("com.google.dagger:hilt-android-compiler:$dagger_version")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:$dagger_viewmodel_version")
    kapt("androidx.hilt:hilt-compiler:$hilt_work_version")
    implementation("androidx.hilt:hilt-work:$hilt_work_version")
    kapt("androidx.hilt:hilt-compiler:$hilt_work_version")

    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.tbuonomo.andrui:viewpagerdotsindicator:4.1.2")
}