# PlacesSearch App

## Background
This app is intended to allow users to search for venues in and around their city or one they are visiting. For the initial version of the app this functionality is limited to the city of Seattle and the surrounding areas. Hopefully this app helps you find something to do in your city!

## Getting Started
In order to run this application, you will need to have a Foursquare API key as well as a Google Maps key. I have left mine in the project for now, but I plan to invalidate them soon, so you will need to create your own. To create your Foursquare and Google accounts please follow the links below:

 [Create a Foursquare account](https://developer.foursquare.com/places-api)
 
 [Obtain a Google Maps key](https://developers.google.com/maps/documentation/maps-static/get-api-key)

These keys are currently located in the `app_config.xml` file. Once these keys are in place you can run the app from Android Studio and the app should work as expected. Please note you will need internet connection to receive search results, map images, etc.

## App Features

1. **Typeahead Search**
    * When you open the app you will see a blank screen with a search bar
    * Start typing to see type ahead suggestions appear on the screen (Note: you must type three characters or more to see these suggestions)
    * Either click on one of the suggestions to perform a search with that query or continue typing and the suggestions will update to match the current query
    * Click the search icon on the keyboard (magnifying glass) to search for venues based on your query
2. **Search for Venues**
    * Once you have submitted your query, a call will be made to retrieve a list of results of venues near Seattle.
    * When these results are returned they will be displayed in a scrollable list.
3. **View Map of Search Results**
    * Once results have been populated in the list, a floating action button (i.e. fab) will appear in the lower right hand corner
    * Click on the fab to load a Google Map with all of the points in the results list
    * Click on a pin to display its name and click on the name to display the details of that venue
4. **View Venue Details**
    * You can view the details of a venue by clicking on it from the results list or by clicking the pin name from the full screen map
    * The venue details will be displayed in a collapsable toolbar layout with a static image detailing the venue in relation to the city center
    * Click on the star button to add/remove the current place as a favorite
5. **Adding/Removing Favorites**
    * Favorites can be added/removed either from the results list or the details pane
    * Favorites status persists across different screens of the app and across app sessions
6. **Dark Mode Support!**
    * Added simple themes to leverage Android support of dark mode and created specific colors for use in dark mode

## Libraries/APIs Used

* **Android Architecture Components**
    * Room was used to implement the favorites database
    * Coroutines were used to simplify the threading model of the app (primarily to fetch results from Foursquare or Google or to send/retrieve information from the favorites database.
    * Data/View Binding was used to monitor loading/error status and to easily update views from the Fragments
    * The Navigation component was used to simplify the transitions between screens and facilitate passing data across fragments using SafeArgs
* **Dagger**
    * Used for dependency injection framework to inject relevant classes into the ViewModels, Repositories, etc.
* **Glide**
    * Used to download venue icons
* **Retrofit**
    * Used as a networking layer to interact with Foursquare and Google APIs
* **Moshi**
    * Used the MoshiConverterFactory  in conjunction with Retrofit to easily parse JSON responses into model objects for easy access
* **Kotlin**
    * Used to reduce boilerplate code and reduce nullability issues
* **Foursquare APIs**
    * Suggestions API -- Type Ahead results
    * Search API -- Searching for venues
    * Venues API -- Getting detailed info about a venue (i.e contact information)
* **Google Maps APIs**
    * Static Map API -- Shows a static map image on the details panel
    * Android API --  Displaying a full screen map view with markers

## Issues Encountered
* I experienced issues when attempting to test my ViewModel classes because I was trying to use the inline mocks library from Mockito and for some reason the mocks for some of my classes were not respecting my when calls to mock methods of those classes. I believe that if I spent more time on it I'd be able to figure these out.
* The Navigation component simplifies certain things about fragment transitions and navigstion states but it does obscure a lot of the functionality which made it hard to tell what was happening a lot of the times, specifically with the ActionBar and back button navigation. I think there are definitely tradeoffs to using this library and writing the transition logic manually.

## Roadmap/Future Improvements
* Add additional unit tests for ViewModel and Repository classes
* Add additional Espresso tests and integrate MockWebServer to fake API responses
* Include support for other cities
* Implement a retry mechanism for failed API calls (i.e exponential backoff)
* UI and style improvements
* Add more information to the details screen (i.e business hours)
* Create a view to list all of a users current favorite places
* Filter search results by distance, type, etc.
