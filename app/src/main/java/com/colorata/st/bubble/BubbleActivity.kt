package com.colorata.st.bubble

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.colorata.st.R
import com.colorata.st.extentions.GenerItems
import com.colorata.st.weather.WeatherResponse
import com.colorata.st.weather.WeatherService
import kotlinx.android.synthetic.main.activity_bubble.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Method

class BubbleActivity : AppCompatActivity() {

    //Init
    private var nightMode = true
    private var flashlightOn = false
    private var baseUrl = "https://api.openweathermap.org/"
    private var appId = "201d8e3dd3a424462228eed61610772d"
    private var city = "London"
    private lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bubble)

        val vibration = getSystemService(VIBRATOR_SERVICE) as Vibrator
        //Init SHAREDPREFS
        sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

        //Creating animations
        createVideos()

        //Component for DON'T KILLING ACTIVITY
        /*val component = ComponentName(applicationContext, AccessibilityService::class.java)
        applicationContext.packageManager.setComponentEnabledSetting(
            component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )*/

        //Configuring NIGHT MODE
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
            Configuration.UI_MODE_NIGHT_NO -> {
                nightMode = false
                logo_bubble.setImageResource(R.drawable.ic_logo_bubble_light)

            }
            Configuration.UI_MODE_NIGHT_YES -> {
                nightMode = true
                logo_bubble.setImageResource(R.drawable.ic_logo_bubble_dark)

            }
        }

        //Getting CONTROLS
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

        //Configuring RECYCLERVIEW
        bubble_recycler.layoutManager = GridLayoutManager(this, 3)
        bubble_recycler.adapter = BubbleAdapter(mutableNames, mutableIcons)

        //Click listener for CONTROLS in RECYCLERVIEW
        bubble_recycler.addOnItemTouchListener(
                RecyclerItemClickListener(bubble_recycler,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            @SuppressLint("WrongConstant")
                            override fun onItemClick(view: View, position: Int) {
                                vibration.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
                                when (mutableNames[position]) {
                                    "Search" -> {

                                        //Going to GOOGLE SEARCH
                                        val i = Intent()
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        i.setClassName(
                                                "com.google.android.googlequicksearchbox",
                                                "com.google.android.apps.gsa.search_gesture.GestureActivity"
                                        )
                                        startActivity(i)
                                    }
                                    "Tethering" -> {

                                        //Going to TETHERING SETTINGS
                                        val i = Intent()
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        i.setClassName(
                                                "com.android.settings",
                                                "com.android.settings.TetherSettings"
                                        )
                                        startActivity(i)
                                    }
                                    "WiFi" -> {

                                        //Going to WIFI SETTINGS
                                        val i = Intent(Settings.ACTION_WIFI_SETTINGS)
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(i)
                                    }
                                    "Flashlight" -> {

                                        //Changing STATE of FLASHLIGHT
                                        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
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

                                        //Going to BLUETOOTH SETTINGS
                                        val i = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(i)
                                    }
                                    "MobData" -> {

                                        //Going to DATA USAGE SETTINGS
                                        val i = Intent(Settings.ACTION_DATA_USAGE_SETTINGS)
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(i)
                                    }
                                    "Nearby Sharing" -> {

                                        //Going to NEARBY SHARING PAGE
                                        val i = Intent()
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        i.setClassName(
                                                "com.google.android.gms",
                                                "com.google.android.gms.nearby.sharing.ReceiveSurfaceActivity"
                                        )
                                        startActivity(i)
                                    }
                                    "Location" -> {

                                        //Going to LOCATION SETTINGS
                                        val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(i)
                                    }
                                    "Calculator" -> {

                                        //Going to GOOGLE CALCULATOR
                                        val i = Intent()
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        i.setClassName(
                                                "com.google.android.calculator",
                                                "com.android.calculator2.Calculator"
                                        )
                                        startActivity(i)
                                    }
                                    "Battery Saver" -> {

                                        //Going TO BATTERY SAVER SETTINGS
                                        val i = Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(i)
                                    }
                                    "Google Tasks" -> {

                                        //Going to GOOGLE TASKS
                                        val i = Intent()
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        i.setClassName(
                                                "com.google.android.apps.tasks",
                                                "com.google.android.apps.tasks.ui.TaskListsActivity"
                                        )
                                        startActivity(i)
                                    }
                                    "Notifications" -> {

                                        //Showing Notifications
                                        val sbservice = getSystemService("statusbar")
                                        val statusbarManager = Class.forName("android.app.StatusBarManager")
                                        val showsb: Method = statusbarManager.getMethod("expandNotificationsPanel")
                                        showsb.invoke(sbservice)
                                    }
                                    else -> {

                                        //Going to USER APP
                                        val i: Intent? = packageManager.getLaunchIntentForPackage(getUserPackage())
                                        i?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(i)
                                    }
                                }
                            }
                        })
        )

        //Getting WEATHER INFO
        getCurrentData()
    }

    override fun onResume() {
        super.onResume()

        //Showing ANIMATIONS
        createVideos()

        //Getting WEATHER INFO
        getCurrentData()
    }

    //Fun for CREATE ANIMATIONS
    private fun createVideos(){
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 700

        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        logo_bubble.startAnimation(animation)
        text_weather.startAnimation(animation)
    }

    //Fun for get WEATHER INFO
    private fun getCurrentData() {

        //Getting city info
        city = sharedPreference.getString("city", "Moscow").toString()

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
                    val stringBuilder =
                            weatherResponse.name?.replace("’", "") + ", " +
                                    weatherResponse.main!!.temp + " \u2103" + "\n" +
                                    "Feels like: " + weatherResponse.main!!.feels + " \u2103"
                    text_weather!!.text = stringBuilder

                }
            }

            //Fun if FAIL
            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                text_weather!!.text = "Error"
            }
        })
    }

    //Getting USER TITLE
    private fun getUserTitle(): String{
        return sharedPreference.getString("userLabel", "User's").toString()
    }

    //Getting USER PACKAGE
    private fun getUserPackage(): String{
        return sharedPreference.getString("userPackage", "com.android.settings").toString()
    }

}