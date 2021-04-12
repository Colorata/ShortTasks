package com.colorata.st.extensions

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.colorata.st.CurrentScreen
import com.colorata.st.activities.SecondaryActivity
import com.colorata.st.ui.theme.Strings
import androidx.core.content.ContextCompat.getSystemService

import android.media.AudioManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat


fun setIsNewUser(context: Context, new: Boolean){
    val shared = context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    shared.edit().putBoolean(Strings.isNewUser, new).apply()
}

fun goToSecondary(context: Context, screen: CurrentScreen){
    val intent = Intent(context, SecondaryActivity::class.java)
    intent.putExtra(Strings.screen, screen)
    context.startActivity(intent)
}

fun changeBrightness(
    context: Context,
    screenBrightnessValue: Int
) {
    Settings.System.putInt(
        context.contentResolver,
        Settings.System.SCREEN_BRIGHTNESS_MODE,
        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
    )
    Settings.System.putInt(
        context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue
    )
}

fun changeMediaVolume(context: Context, percents: Int){

    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
    val max = audioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
    val value = (max/100)*percents
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value.toInt(), 0)
}

fun changeRingVolume(context: Context, percents: Int){

    val audioManagerRing = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
    val maxRing = audioManagerRing!!.getStreamMaxVolume(AudioManager.STREAM_RING).toFloat()
    val valueRing = (maxRing/100)*percents
    audioManagerRing.setStreamVolume(AudioManager.STREAM_RING, valueRing.toInt(), 0)

    val audioManagerNotification = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
    val maxNotification = audioManagerNotification!!.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION).toFloat()
    val valueNotification = (maxNotification/100)*percents
    audioManagerNotification.setStreamVolume(AudioManager.STREAM_NOTIFICATION, valueNotification.toInt(), 0)
}

fun enableAutoRotate(context: Context, enabled: Boolean) {
    val enable = if (enabled) 1 else 0
    Log.d("enable", enable.toString())
    Settings.System.putInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, enable)
}

fun enableDND(context: Context, enabled: Boolean) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (enabled) notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)
    else notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
}

fun enableNightLight(enabled: Boolean) {
    if (enabled) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
}

fun enableFlightMode(context: Context, enabled: Boolean) {
    val enable = if (enabled) 1 else 0
    Settings.Global.putInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, enable)
}