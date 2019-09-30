# Weather-App
A simple demo project for Weather-App based on <b>MVVM clean architecture</b>.

<img src="https://github.com/irahulyadav/weather-app/blob/master/device-2019-09-30-213105.png" width="200" style="max-width:100%;"> </br></br>

## [Download APK](https://github.com/irahulyadav/weather-app/raw/master/Weather-1.0-debug.apk)
#### App Features
* Users can see current place weather detail.


#### App Architecture 
Based on mvvm architecture and repository pattern.
 
 #### The app includes the following main components:
* A web api service.
* A repository that works with  the api service, providing a unified data interface.
* A ViewModel that provides data specific for the UI.
* Unit Test cases for API service


#### App Packages
* <b>api.service</b> - contains the api classes to make api calls to server, using Retrofit. 
* <b>di</b> - contains dependency injection classes, using Dagger2.
* <b>viewmodel</b> - contains classes related to viewmodel.
* <b>repository</b> - contains the repositiry classes to cache network data and other operation.


#### App Specs
* Minimum SDK 23
* Kotline - '1.3.50'
* MVVM Architecture
* Android Architecture Components (LiveData, Lifecycle, ViewModel, ConstraintLayout)
* [AndroidX](https://developer.android.com/jetpack/androidx) androidx support libraries
* [RxJava2](https://github.com/ReactiveX/RxJava) for implementing Observable pattern.
* [Dagger 2](https://google.github.io/dagger/) for dependency injection.
* [Retrofit 2](https://square.github.io/retrofit/) for API integration.
* [Okhhtp3](https://github.com/square/okhttp) for implementing interceptor, logging and mocking web server.
* [Robolectric](http://robolectric.org/) for implementing unit test case

## [Download APK](https://github.com/irahulyadav/weather-app/raw/master/Weather-1.0-debug.apk)
