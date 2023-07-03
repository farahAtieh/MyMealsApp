import com.codingfeline.buildkonfig.compiler.FieldSpec.Type

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization").version("1.8.21")
    id("com.codingfeline.buildkonfig")
    id("com.squareup.sqldelight")
    id("com.google.devtools.ksp").version("1.8.21-1.0.11")
    id("com.rickclephas.kmp.nativecoroutines").version("1.0.0-ALPHA-9")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val koin_version = "3.4.0"
        val ktor_version = "2.3.0"
        val coroutines_version = "1.7.1"
        val sqlDelight_version = "1.5.5"

        val commonMain by getting {
            dependencies {
                //koin
                api("io.insert-koin:koin-core:$koin_version")
                //ktor
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-json:$ktor_version")
                implementation("io.ktor:ktor-client-logging:$ktor_version")
                implementation("io.ktor:ktor-client-serialization:$ktor_version")
                implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
                //coroutine
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
                //sqlDelight
                implementation("com.squareup.sqldelight:runtime:$sqlDelight_version")
                implementation( "com.squareup.sqldelight:coroutines-extensions:$sqlDelight_version")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                //ktor client
                implementation("io.ktor:ktor-client-android:$ktor_version")
                //sqlDelight android driver
                implementation("com.squareup.sqldelight:android-driver:$sqlDelight_version")
                //koin
                api("io.insert-koin:koin-android:$koin_version")
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                //ktor client
                implementation("io.ktor:ktor-client-ios:$ktor_version")
                //sqlDelight ios driver
                implementation("com.squareup.sqldelight:native-driver:$sqlDelight_version")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

        kotlin.sourceSets.all{
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }
    }
}

android {
    namespace = "com.example.mymealsapp"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}


sqldelight{
    database("MealsDatabase"){
        packageName = "com.example.mymealsapp.db"
        sourceFolders = listOf("sqldelight")
        deriveSchemaFromMigrations = true
    }
}

buildkonfig {
    packageName = "com.example.mymealsapp"
    val baseUrl = "baseUrl"
    defaultConfigs {
        buildConfigField(
            Type.STRING,
            baseUrl,
            "https://www.themealdb.com/"
        )
    }
}