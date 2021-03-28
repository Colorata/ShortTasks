 package com.colorata.st

import android.annotation.SuppressLint
import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.colorata.st.extentions.GetTheme
import com.colorata.st.sheets.*
import com.colorata.st.sheets.welcome.WelcomeDialog
import kotlinx.android.synthetic.main.activity_bubble.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bubble_manager
import kotlinx.android.synthetic.main.first_change_alert.*
import kotlinx.android.synthetic.main.help_alert.*
import java.util.*


 class MainActivity : AppCompatActivity() {

    //Init values
    private var nightMode = true
    private lateinit var sharedPreference: SharedPreferences



    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Configuring SHAREDPREFS
        sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

        //TODO: fix bug with crashing
        packageManager.setComponentEnabledSetting(ComponentName(this@MainActivity, DarkApp::class.java),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)
        packageManager.setComponentEnabledSetting(ComponentName(this@MainActivity, LightApp::class.java),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)

        //Setting up NIGHT MODE
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
            Configuration.UI_MODE_NIGHT_NO -> {
                nightMode = false
                logo_main.setImageResource(R.drawable.ic_logo_main_light)
                packageManager.setComponentEnabledSetting(ComponentName(this@MainActivity, DarkApp::class.java),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP)
                packageManager.setComponentEnabledSetting(ComponentName(this@MainActivity, LightApp::class.java),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                nightMode = true
                logo_main.setImageResource(R.drawable.ic_logo_main_dark)
                packageManager.setComponentEnabledSetting(ComponentName(this@MainActivity, LightApp::class.java),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP)
                packageManager.setComponentEnabledSetting(ComponentName(this@MainActivity, DarkApp::class.java),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP)
            }
        }

        sharedPreference.edit().putBoolean("nightMode", nightMode).apply()

        if (sharedPreference.getInt("welcome", 0) < 1) {
            val editor = sharedPreference.edit()
            editor.putInt("welcome", sharedPreference.getInt("welcome", 0) + 1)
            editor.apply()
            WelcomeDialog(this, layoutInflater, sharedPreference).show
        }

        //Creating ANIMATIONS and configuring GUIDELINES
        createVideos()
        guidelineConfig()

        //Setting up ONCLICKLISTENER
        help.setOnClickListener{ HelpDialog(this, layoutInflater).show }
        about.setOnClickListener { AboutDialog(this, layoutInflater).show }
        settings.setOnClickListener { SettingsDialog(this, layoutInflater).show }
        bubble_manager.setOnClickListener { BubbleManagerDialog(this, layoutInflater).show }
        weather_director.setOnClickListener { WeatherDirector(this, layoutInflater).show }

        //Changing COLOR TO ELEMENTS
        text_main.setTextColor(GetTheme(this).button)
        help.setTextColor(GetTheme(this).button)

        about.setTextColor(GetTheme(this).button)

        settings.setTextColor(GetTheme(this).button)

        bubble_manager.setTextColor(GetTheme(this).button)

        weather_director.setTextColor(GetTheme(this).button)

        main_layout.setBackgroundColor(GetTheme(this).back)

        window.navigationBarColor = GetTheme(this).back
        window.statusBarColor = GetTheme(this).back
    }

    //Fun for creating ANIMATIONS
    private fun createVideos(){
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 700

        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        logo_main.startAnimation(animation)
        text_main.startAnimation(animation)
    }

    //Fun for configuring GUIDELINES
    private fun guidelineConfig(){
        val displayMetrics: DisplayMetrics = baseContext.resources.displayMetrics
        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        guideline_one_handed_main.setGuidelineBegin((dpHeight * 0.37).toInt())
        guideline_logo_main.setGuidelineBegin((dpWidth * 0.37).toInt())
    }
}