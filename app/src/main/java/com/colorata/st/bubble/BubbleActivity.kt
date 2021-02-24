package com.colorata.st.bubble

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.colorata.st.R
import com.colorata.st.RecyclerItemClickListener
import com.colorata.st.WeatherResponse
import com.colorata.st.extentions.GenerItems
import com.colorata.st.weather.WeatherService
import kotlinx.android.synthetic.main.activity_bubble.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Method

class BubbleActivity : AppCompatActivity() {

    private var flashlightOn = false
    private var baseUrl = "https://api.openweathermap.org/"
    private var appId = "201d8e3dd3a424462228eed61610772d"
    private var city = "London"
    private lateinit var sharedPreference: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bubble)
        sharedPreference =  getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
        createVideos()
        /*val component = ComponentName(applicationContext, AccessibilityService::class.java)
        applicationContext.packageManager.setComponentEnabledSetting(
            component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )*/
        val mutableNames = mutableListOf<String>()
        val mutableIcons = mutableListOf<Int>()
        for (i in 0..GenerItems().names().lastIndex){

            mutableNames.add(
                sharedPreference.getString(
                    "name $i",
                    GenerItems().names()[i]
                ).toString()
            )
            mutableIcons.add(sharedPreference.getInt("icon $i", GenerItems().icons()[i]))
        }
        mutableNames.add(getUserTitle())
        mutableIcons.add(R.drawable.ic_baseline_face_24)
        bubble_recycler.layoutManager = GridLayoutManager(this, 3)
        bubble_recycler.adapter = BubbleAdapter(mutableNames, mutableIcons)

        bubble_recycler.addOnItemTouchListener(
            RecyclerItemClickListener(bubble_recycler,
                object : RecyclerItemClickListener.OnItemClickListener {
                    @SuppressLint("WrongConstant")
                    override fun onItemClick(view: View, position: Int) {
                        when (mutableNames[position]) {
                            "Search" -> {
                                val i = Intent()
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                i.setClassName(
                                    "com.google.android.googlequicksearchbox",
                                    "com.google.android.apps.gsa.search_gesture.GestureActivity"
                                )
                                startActivity(i)
                            }
                            "Tethering" -> {
                                val i = Intent()
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                i.setClassName(
                                    "com.android.settings",
                                    "com.android.settings.TetherSettings"
                                )
                                startActivity(i)
                            }
                            "WiFi" -> {
                                val i = Intent(Settings.ACTION_WIFI_SETTINGS)
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(i)
                            }
                            "Flashlight" -> {
                                val cameraManager =
                                    getSystemService(CAMERA_SERVICE) as CameraManager
                                if (!flashlightOn) {
                                    flashlightOn = !flashlightOn
                                    val cameraId = cameraManager.cameraIdList[0]
                                    cameraManager.setTorchMode(cameraId, flashlightOn)
                                } else if (flashlightOn) {
                                    flashlightOn = !flashlightOn
                                    val cameraId = cameraManager.cameraIdList[0]
                                    cameraManager.setTorchMode(cameraId, flashlightOn)
                                }
                            }
                            "Bluetooth" -> {
                                val i = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(i)
                            }
                            "MobData" -> {
                                val i = Intent(Settings.ACTION_DATA_USAGE_SETTINGS)
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(i)
                            }
                            "Nearby Sharing" -> {
                                val i = Intent()
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                i.setClassName(
                                    "com.google.android.gms",
                                    "com.google.android.gms.nearby.sharing.ReceiveSurfaceActivity"
                                )
                                startActivity(i)
                            }
                            "Location" -> {
                                val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(i)
                            }
                            "Calculator" -> {
                                val i = Intent()
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                i.setClassName(
                                    "com.google.android.calculator",
                                    "com.android.calculator2.Calculator"
                                )
                                startActivity(i)
                            }
                            "Battery Saver" -> {
                                val i = Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(i)
                            }
                            "Google Tasks" -> {
                                val i = Intent()
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                i.setClassName(
                                    "com.google.android.apps.tasks",
                                    "com.google.android.apps.tasks.ui.TaskListsActivity"
                                )
                                startActivity(i)
                            }
                            "Notifications" -> {
                                val sbservice = getSystemService("statusbar")
                                val statusbarManager = Class.forName("android.app.StatusBarManager")
                                val showsb: Method = statusbarManager.getMethod("expandNotificationsPanel")
                                showsb.invoke(sbservice)
                            }
                            else -> {
                                val i = Intent()
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                i.setClassName(getUserPackage(), getUserActivity())
                                startActivity(i)
                            }
                        }
                    }
                })
        )
        getCurrentData()
    }

    override fun onResume() {
        super.onResume()
        createVideos()
        getCurrentData()
    }

    private fun createVideos(){
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 700

        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        logo_bubble.startAnimation(animation)
        text_weather.startAnimation(animation)
    }

    private fun getCurrentData() {
        city = sharedPreference.getString("city", "Moscow").toString()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(city, "metric", appId)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!

                    val stringBuilder =
                        weatherResponse.name?.replace("’", "") + ", " +
                        weatherResponse.main!!.temp + " \u2103" + "\n" +
                                "Feels like: " + weatherResponse.main!!.feels + " \u2103"
                    text_weather!!.text = stringBuilder

                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                text_weather!!.text = t.message
            }
        })
    }

    private fun getUserTitle(): String{
        return sharedPreference.getString("title", "User's").toString()
    }

    private fun getUserPackage(): String{
        return sharedPreference.getString("package", "com.android.settings").toString()
    }

    private fun getUserActivity(): String{
        return sharedPreference.getString("activity", "com.android.settings.TetherSettings").toString()
    }

}