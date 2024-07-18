import java.util.Properties

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.jetbrains.kotlin.android)
}

fun getKey(key: String): String {
	val properties = Properties()
	val tokenFile = project.rootProject.file("local.properties")
	properties.load(tokenFile.inputStream())
	return properties.getProperty(key)
}

android {
	namespace = "com.roland.android.remotedatasource"
	compileSdk = 34

	defaultConfig {
		minSdk = 24

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildFeatures {
		buildConfig = true
	}

	buildTypes {
		debug {
			buildConfigField("String", "API_KEY", getKey("API_KEY"))
			buildConfigField("String", "APP_ID", getKey("APP_ID"))
			buildConfigField("String", "ORGANIZATION_ID", getKey("ORGANIZATION_ID"))
		}
		release {
			buildConfigField("String", "API_KEY", getKey("API_KEY"))
			buildConfigField("String", "APP_ID", getKey("APP_ID"))
			buildConfigField("String", "ORGANIZATION_ID", getKey("ORGANIZATION_ID"))

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
}

dependencies {

	// android
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)

	// di
	implementation(platform(libs.koin.bom))
	implementation(libs.koin.core)

	// networking
	implementation(libs.moshi)
	implementation(libs.moshiKotlin)
	implementation(libs.okHttp)
	implementation(libs.retrofit)
	implementation(libs.retrofitMoshi)

	// other modules
	implementation(project(path = ":domain"))

	// test
	testImplementation(libs.coroutines)
	testImplementation(libs.junit)
	testImplementation(libs.mockito)

}