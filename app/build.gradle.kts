import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("io.gitlab.arturbosch.detekt") version "1.18.1"
    id("org.jlleitschuh.gradle.ktlint") version "11.1.0"
}

android {
    namespace = "com.example.friends"
    compileSdk = 35

    ktlint {
        android = true // Enable Android-specific linting rules
        ignoreFailures = false // Fail the build if KtLint finds any issues
        disabledRules.set(
            listOf(
                "final-newline",
                "no-wildcard-imports",
                "max-line-length",
                "import-ordering",
                "no-consecutive-blank-lines",
                "spacing"
            )
        )

        reporters {
            reporter(ReporterType.HTML) // Output KtLint results in plain text format
            reporter(ReporterType.PLAIN) // Output KtLint results in HTML format
        }
    }

    lint {
        // Turns off checks for the issue IDs you specify.
        disable += "TypographyFractions" + "TypographyQuotes"
        // Turns on checks for the issue IDs you specify. These checks are in
        // addition to the default lint checks.
        enable += "RtlHardcoded" + "RtlCompat" + "RtlEnabled"
        // To enable checks for only a subset of issue IDs and ignore all others,
        // list the issue IDs with the 'check' property instead. This property overrides
        // any issue IDs you enable or disable using the properties above.
        checkOnly += "NewApi" + "InlinedApi"
        // If set to true, turns off analysis progress reporting by lint.
        quiet = true
        // If set to true (default), stops the build if errors are found.
        abortOnError = false
        // If set to true, lint only reports errors.
        ignoreWarnings = true
        // If set to true, lint also checks all dependencies as part of its analysis.
        // Recommended for projects consisting of an app with library dependencies.
        checkDependencies = true
    }

    detekt {
        config = files("$rootDir/config/detekt/detekt.yml")
        buildUponDefaultConfig = true
        ignoreFailures = true
        baseline = file("$rootDir/config/detekt/baseline.xml")

        source = files(
            "src/main/java",
            "src/main/kotlin"
        )
    }


    defaultConfig {
        applicationId = "com.example.friends"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // detekt
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.17.1")
    testImplementation("io.gitlab.arturbosch.detekt:detekt-api:1.17.1")
    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.17.1")
    testImplementation("org.assertj:assertj-core:3.19.0")
}