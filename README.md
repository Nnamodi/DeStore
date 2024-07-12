# DeStore
This sample shopping app is a task from HNG11 stage 3.

## Screenshots
|::::::::::::::::::::::::::::::::::::::::|
|:--:|
|![App screen](screenshots/app_screen.png)|

## Features
* Shows list of products fetched from remote source
* Requires internet connection

## About

* It has only a screens that shows a list of products/items remotely fetched from the [Timbu API](https://docs.timbu.cloud/api/intro).
* Clean and Simple Material UI

* This project was built with Clean Architecture and MVVM pattern.

    There are two _modules_ in this project:

    `app` - The main module serving as the user interface/communication layer

    `dataRemoteSource` - Fetches data from internet and manipulates it
<br/>

  ***Explore the virtualized demonstration of the release apk version [here](https://appetize.io/app/b_qgxzowdyi4gbo6jlblutukpyne)***

  ***Or you can install the apk from below 👇🏽***

  [![DeStore App](https://img.shields.io/badge/DeStore-APK-blue.svg?style=for-the-badge&logo=android)](https://github.com/Nnamodi/DeStore/releases/download/v1.0/app-release.apk)

## Build Instructions
You can build the app via Android Studio, or by running the following command in your directory:

    ./gradlew <assembleDebug | assembleRelease>

   You can then find the apk file in `DeStore/app/build/outputs/apk`

## Developed with
* [Jetpack Compose](https://developer.android.com/jetpack/compose) - Jetpack Compose is Android’s recommended modern toolkit for building native UI.
* [Coil-kt](https://coil-kt.github.io/coil/compose) - Image loading for Android and Compose Multiplatform.
* [Koin](https://insert-koin.io/docs/setup/koin/) - Reduces boilerplate of doing manual dependency injection in your project.
* [Material3](https://m3.material.io) - Google's latest design system with adaptable components and tools that support the best practices of user interface design for building beautiful apps.
* [Kotlin Coroutine](https://kotlinlang.org/docs/coroutines-overview.html) - For executing tasks asynchronously.
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
* [Retrofit](https://square.github.io/retrofit) - A type-safe HTTP client for Android and Java.
* [Moshi](https://sqaure.github.io/moshi) - A modern JSON library for Kotlin and Java.
* [OkHttp](https://sqaure.github.io/okhttp) - An efficient HTTP & HTTP/2 client for Android and Java.
