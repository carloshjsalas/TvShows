## TvShows
This is an example app with 2022 technologies used for native Android development.
Is designed and coded to fetch data from an REST API service, saving this data locally and presenting it to the user.

The data comes from the public API service [TheMovieDb](https://developers.themoviedb.org/3/getting-started/introduction), specifically:
- [Poputal Tv Shows](https://developers.themoviedb.org/3/tv/get-popular-tv-shows) to get the entire list of popupal tv shows.
- [Tv Show Details](https://developers.themoviedb.org/3/tv/get-tv-details) to get an specific tv show details.

### Overview
The app contains to 2 screens. 
- **1** The first one is the landing page (Home Screen), that is a vertical stagered list view, with "infinite scroll", where the user can see a list of tv shows.

     <img width="220" alt="image" src="https://user-images.githubusercontent.com/8594582/173067045-52dcdba9-e64b-4f68-81e6-5d1bbc3e46d8.png">

- **2** The second one is the tv show details screen, that contains the most important details of each tv show.

    <img width="220" alt="image" src="https://user-images.githubusercontent.com/8594582/173067380-8f20229b-3805-4ded-8b4b-1260517d8389.png">


In order to get the data, when the user opens the app, in the home screen a call to get the first page of the tv shows is triggered. Next, this data is presented to the user, and at the same time un saved locally in an internal database. If for some reason the user lose his internet connection, the app will try to get the data from the local data base.
The same behaviour for the details screen, in order to get the tv show details.

### Architecture MVVM
The app is structured in 3 main parts:
- **1 The view layer**: consisting of the fragments and activity (sort of the main view controllers) and all other ui related components. It interacts with the viewmodel to handle events on the ui and to get informed about the ui state.

- **2 The viewmodel**: that determines the state of the ui and contains logic regarding user events. It interacts with a repository to set or get data from cache (memory or local storage) or on the network.

- **3 The data layer**: represented by (in this case) a repository that is responsible for delivering data to the app (and server) and contains logic regarding network requests, caching, etc.

<img width="626" alt="image" src="https://user-images.githubusercontent.com/8594582/173066355-1e3d90b4-a771-493e-afa3-f6c6f32ab829.png">

Also simple unit tests for both view models where addded, just to show how this should be tested.

### Technologies used

Kotlin Android Native, MVVM, DagerHilt, Navigation Component, Material Component 3, Retrofit, Moshi, Room, Coroutines, Kotlin Flow, LiveData, ViewModel, JUnit, Mockito.

