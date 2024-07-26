<<<<<<< HEAD
val kotlin_version: String by project
val logback_version: String by project

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    //id("io.ktor.plugin") version "2.3.12"
}

android {
    namespace = "com.example.server_2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.server_2"
=======
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.server_1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.server_1"
>>>>>>> c694a3eb56bbe49e06464c6b630a90defea3744e
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
<<<<<<< HEAD
=======
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
            }
        }
>>>>>>> c694a3eb56bbe49e06464c6b630a90defea3744e
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
<<<<<<< HEAD
=======
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
>>>>>>> c694a3eb56bbe49e06464c6b630a90defea3744e
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
<<<<<<< HEAD
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

/*
    //androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-websockets")
    implementation("io.ktor:ktor-network-tls")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests")
*/

    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")

    //testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
=======
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
>>>>>>> c694a3eb56bbe49e06464c6b630a90defea3744e
}