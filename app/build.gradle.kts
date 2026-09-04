plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.lamps.sdk.demo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lamps.sdk.demo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

val lampsVersion = providers.gradleProperty("lampsVersion").orElse("1.0.0").get()

dependencies {
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    implementation("io.github.hoorahtech:sdk:$lampsVersion")
    implementation("io.github.hoorahtech:pangle:$lampsVersion")
    implementation("io.github.hoorahtech:ylh:$lampsVersion")
    implementation("io.github.hoorahtech:noah:$lampsVersion")
    debugImplementation("io.github.hoorahtech:sdk-tools:$lampsVersion")

    // Local AAR. files() has no POM transitives, so also include core / vendor AARs.
    // implementation(
    //     files(
    //         "libs/lamps-core-1.0.0.aar",
    //         "libs/lamps-sdk-1.0.0.aar",
    //         "libs/lamps-pangle-1.0.0.aar",
    //         "libs/lamps-ylh-1.0.0.aar",
    //         "libs/lamps-noah-1.0.0.aar",
    //         "libs/ads-sdk-pro-7.6.1.2.aar",
    //         "libs/noah-15.1.4002.aar",
    //     )
    // )
    // debugImplementation(
    //     files(
    //         "libs/lamps-sdk-tools-1.0.0.aar",
    //         "libs/ads-sdk-tools-7.6.4.2.aar",
    //     )
    // )
}
