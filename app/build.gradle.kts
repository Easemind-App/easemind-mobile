plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.example.easemind"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.easemind"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            keyAlias = "androidreleasekey"
            keyPassword = "Easemind"
            storeFile = file("../release.keystore")
            storePassword = "Easemind"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
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
        buildConfig = true
        compose = false
        mlModelBinding = true

        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.1"
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }

        }

        dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.appcompat)
            implementation(libs.material)
            implementation(libs.androidx.activity)
            implementation(libs.androidx.constraintlayout)
            implementation(libs.androidx.camera.camera2)
            implementation(libs.camera.lifecycle)
            implementation(libs.camera.view)
            implementation(libs.play.services.auth)
            implementation(libs.glide)
            implementation(libs.retrofit)
            implementation(libs.converter.gson)
            implementation(libs.logging.interceptor)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.androidx.lifecycle.viewmodel.ktx)
            implementation(libs.androidx.lifecycle.livedata.ktx)
            implementation(libs.androidx.activity.ktx)
            testImplementation(libs.junit)
            androidTestImplementation(libs.androidx.junit)
            androidTestImplementation(libs.androidx.espresso.core)
            implementation("androidx.fragment:fragment-ktx:1.7.1")
            implementation("com.github.bumptech.glide:glide:4.16.0")
            implementation("com.squareup.retrofit2:retrofit:2.9.0")
            implementation("com.squareup.retrofit2:converter-gson:2.9.0")
            implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
            implementation("org.tensorflow:tensorflow-lite-task-vision:0.4.4")
            implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
            implementation("org.tensorflow:tensorflow-lite-metadata:0.4.4")
        }
    }
}
