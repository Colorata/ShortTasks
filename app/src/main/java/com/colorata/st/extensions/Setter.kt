package com.colorata.st.extensions

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.provider.Settings
import android.service.controls.ControlsProviderService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.colorata.st.CurrentScreen
import com.colorata.st.activities.SecondaryActivity
import com.colorata.st.ui.theme.Strings
import java.lang.reflect.Method


fun Context.goToSecondary(screen: CurrentScreen) {
    val intent = Intent(this, SecondaryActivity::class.java)
    intent.putExtra(Strings.screen, screen)
    startActivity(intent)
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

fun Context.changeMediaVolume(percents: Int) {

    val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager?
    val max = audioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
    val value = (max / 100) * percents
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value.toInt(), 0)
}

fun Context.changeRingVolume(percents: Int) {

    val audioManagerRing = getSystemService(Context.AUDIO_SERVICE) as AudioManager?
    val maxRing = audioManagerRing!!.getStreamMaxVolume(AudioManager.STREAM_RING).toFloat()
    val valueRing = (maxRing / 100) * percents
    audioManagerRing.setStreamVolume(AudioManager.STREAM_RING, valueRing.toInt(), 0)

    val audioManagerNotification = getSystemService(Context.AUDIO_SERVICE) as AudioManager?
    val maxNotification =
        audioManagerNotification!!.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION).toFloat()
    val valueNotification = (maxNotification / 100) * percents
    audioManagerNotification.setStreamVolume(
        AudioManager.STREAM_NOTIFICATION,
        valueNotification.toInt(),
        0
    )
}

fun Context.enableAutoRotate(enabled: Boolean) {
    val enable = if (enabled) 1 else 0
    Settings.System.putInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, enable)
}

fun enableBluetooth(enabled: Boolean) {
    val adapter = BluetoothAdapter.getDefaultAdapter()
    if (enabled) adapter.enable() else adapter.disable()
}

fun Context.enableFlashlight(enabled: Boolean) {
    val cameraManager = getSystemService(ControlsProviderService.CAMERA_SERVICE) as CameraManager
    val cameraId = cameraManager.cameraIdList[0]
    cameraManager.setTorchMode(cameraId, enabled)
}

@SuppressLint("WrongConstant")
fun Context.showNotifications() {
    val service = getSystemService("statusbar")
    val statusBarManager = Class.forName("android.app.StatusBarManager")
    val show: Method = statusBarManager.getMethod("expandNotificationsPanel")
    show.invoke(service)
}

fun Context.hidePowerMenu() {
    val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
    intent.putExtra("action", AccessibilityService.GLOBAL_ACTION_POWER_DIALOG)
    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
}