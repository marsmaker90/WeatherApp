Weather APP
====

Weather App is an sample project to display the weather report for a relative period.


### To get the weather report

Use `getWeatherData` , which emits observable data of Weather report.

 * **getWeatherData** Used to get the Weather Report in Main thread , which is mainly used for UI related stuff's.
 
```Kotlin 
fun getWeatherData(mLatitude: Double, mLongitude: Double): Observable<WeatherData> {
        return ServiceHandler.getApi().getWeather(mLatitude, mLongitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
```

Sample:-
 
```Kotlin 
        viewModel.getWeatherData(12.9514147, 80.2436142)
            .subscribe({it},{it.message})

```

 * **getWeatherDataUT** Used to get the Weather Report in Non-UI thread , which is mainly used for unit testing.
 
```Kotlin 
fun getWeatherDataUT(mLatitude: Double, mLongitude: Double): Observable<WeatherData> {
        return ServiceHandler.getApi().getWeather(mLatitude, mLongitude)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
    }
```

Sample:-
 
```Kotlin 
        viewModel.getWeatherDataUT(12.9514147, 80.2436142)
            .subscribe({it},{it.message})

```
 ### Things covered in this sample:
 
  * **Kotlin** - Used Kotlin as development language
              - fixes a series of issues that Java suffers
              - Adapts Extension feature which helps a lot to avoid unneccessary utility classes
 * **MVVM**  - Used MVVM as a design pattern for this project
 * **Retrofit** - Used for API Handling
 * **RxJava2 & RxAndroid** - Used for consuming the datum as composite disposables
                           - Used as an adapter between API and View Model
                           - Used for async calls
 * **Fused Location Provider Client** - To get the location details
 * **Proguard Rules** - Which help in securing the APK from reverse engineering also minify the build
 * **Weather Report API** - Used "https://darksky.net" API's 
      
               
 
