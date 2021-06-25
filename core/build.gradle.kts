plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
}

dependencies {
    implementation(project(":core_network"))

    //ViewBindDelegate
    implementation("com.github.kirich1409:viewbindingpropertydelegate:1.4.4")
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.4.6")
}