package com.colorata.st.extensions.weather

import com.google.gson.annotations.SerializedName


//Weather info PARSER
class WeatherResponse {

    /*@SerializedName("coord")
    var coord: Coord? = null
    @SerializedName("sys")
    var sys: Sys? = null*/
    @SerializedName("weather")
    var weather = ArrayList<Weather>()
    @SerializedName("main")
    var main: Main? = null
    /*@SerializedName("wind")
    var wind: Wind? = null
    @SerializedName("rain")
    var rain: Rain? = null
    @SerializedName("clouds")
    var clouds: Clouds? = null
    @SerializedName("dt")
    var dt: Float = 0.toFloat()*/
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("name")
    var name: String? = null
    /*@SerializedName("cod")
    var cod: Float = 0.toFloat()*/
}

class Weather {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("main")
    var main: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("icon")
    var icon: String? = null
}

/*class Clouds {
    @SerializedName("all")
    var all: Float = 0.toFloat()
}*/

/*class Rain {
    @SerializedName("3h")
    var h3: Float = 0.toFloat()
}*/

/*class Wind {
    @SerializedName("speed")
    var speed: Float = 0.toFloat()
    @SerializedName("deg")
    var deg: Float = 0.toFloat()
}*/

class Main {
    @SerializedName("feels_like")
    var feels: Float = 0.toFloat()
    @SerializedName("temp")
    var temp: Float = 0.toFloat()
    /*@SerializedName("humidity")
    var humidity: Float = 0.toFloat()
    @SerializedName("pressure")
    var pressure: Float = 0.toFloat()
    @SerializedName("temp_min")
    var temp_min: Float = 0.toFloat()
    @SerializedName("temp_max")
    var temp_max: Float = 0.toFloat()*/
}

/*class Sys {
    @SerializedName("country")
    var country: String? = null
    @SerializedName("sunrise")
    var sunrise: Long = 0
    @SerializedName("sunset")
    var sunset: Long = 0
}*/

/*class Coord {
    @SerializedName("lon")
    var lon: Float = 0.toFloat()
    @SerializedName("lat")
    var lat: Float = 0.toFloat()
}*/