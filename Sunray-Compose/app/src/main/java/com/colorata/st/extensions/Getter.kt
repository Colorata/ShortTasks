package com.colorata.st.extensions

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.hardware.camera2.CameraManager
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.colorata.st.extensions.weather.WeatherResponse
import com.colorata.st.extensions.weather.WeatherService
import com.colorata.st.ui.theme.Strings
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable
import java.lang.reflect.Method

@Composable
fun getBottomNavigationHeight(): Dp {
    val shared = LocalContext.current.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    return shared.getInt(Strings.bottomSize, 30).dp + getNavBarHeight()
}

@Composable
fun getNavBarHeight(): Dp {
    var navigationBarHeight = 0
    val resourceId: Int = LocalContext.current.resources.getIdentifier(
        "navigation_bar_height",
        "dimen",
        "android"
    )
    if (resourceId > 0) {
        navigationBarHeight = LocalContext.current.resources.getDimensionPixelSize(resourceId)
    }
    return pxToDp(navigationBarHeight).dp
}

@Composable
fun isNewUser(): Boolean{
    val shared = LocalContext.current.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    return shared.getBoolean(Strings.isNewUser, true)
}

fun getCurrentWeather(context: Context): MutableList<Serializable> {

    //TODO: fix bug current is null after response
    val baseUrl = "https://api.openweathermap.org/"
    val appId = "201d8e3dd3a424462228eed61610772d"

    val shared = context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)

    var currentRight = ""
    var currentFeels = ""
    var currentFloat = 0f

    //Getting city info
    val city = shared.getString("city", "Moscow").toString()

    //Building service to get info
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(WeatherService::class.java)
    val call = service.getCurrentWeatherData(city, "metric", appId)

    //CALLING
    call.enqueue(object : Callback<WeatherResponse> {

        //Fun if SUCCESS
        override fun onResponse(
            call: Call<WeatherResponse>,
            response: Response<WeatherResponse>
        ) {
            if (response.code() == 200) {
                val weatherResponse = response.body()!!

                //Configuring TEXT
                currentRight = weatherResponse.name?.replace("’", "")!!
                currentFeels = "Feels: " + weatherResponse.main!!.feels + " \u2103"
                currentFloat = weatherResponse.main!!.temp

                val edit = shared.edit()
                edit.putString("CurrentRightWeather", currentRight)
                edit.putString("CurrentFeelsWeather", currentFeels)
                edit.putFloat("CurrentFloatWeather", currentFloat)
                edit.apply()
                /*Log.d("Weather", currentRight)
                Log.d("Weather", currentFeels)
                Log.d("Weather", currentFloat.toString())*/
            }
        }

        //Fun if FAIL
        @SuppressLint("SetTextI18n")
        override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
            currentRight = "Error"
            currentFeels = "Error"
        }
    })
    return mutableListOf(currentRight, currentFeels, currentFloat)
}

fun isWifiEnabled(context: Context): Boolean {
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    return wifiManager.isWifiEnabled
}

fun isBluetoothEnabled(): Boolean {
    val adapter = BluetoothAdapter.getDefaultAdapter()
    return adapter.isEnabled
}

fun isLocationEnabled(mContext: Context): Boolean {
    val lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER)
}

fun isMobileDataEnabled(context: Context): Boolean =
    Settings.Secure.getInt(context.contentResolver, "mobile_data", 1) == 1

fun isBatterySaverEnabled(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    return powerManager.isPowerSaveMode
}

fun isHotspotEnabled(context: Context): Boolean {
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val method: Method = wifiManager.javaClass.getDeclaredMethod("getWifiApState")
    method.isAccessible = true
    val actualState = method.invoke(wifiManager, null as Array<Any?>?) as Int
    return actualState == 13
}