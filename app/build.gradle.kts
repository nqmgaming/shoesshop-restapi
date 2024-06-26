plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.nqmgaming.shoesshop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nqmgaming.shoesshop"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    val ev_version = "1.0.4"
    implementation ("com.wajahatkarim:easyvalidation-core:$ev_version")

    // Shows Toasts by default for every validation error
    implementation ("com.wajahatkarim:easyvalidation-toast:$ev_version")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Jwt Decoder
    implementation ("com.auth0.android:jwtdecode:2.0.2")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //popup dialog
    implementation ("com.saadahmedev.popup-dialog:popup-dialog:1.0.5")

    // Avatar Image View
    implementation("xyz.schwaab:avvylib:1.2.0")

    // Ted Permission
    implementation("io.github.ParkSangGwon:tedpermission-coroutine:3.3.0")

    // Image Picker
    implementation ("com.github.dhaval2404:imagepicker:2.1")

}