package com.colorata.st.extensions

import android.app.NotificationManager
import android.app.UiModeManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.provider.Settings
import android.service.controls.ControlsProviderService
import com.colorata.st.CurrentScreen
import com.colorata.st.activities.SecondaryActivity
import com.colorata.st.ui.theme.Strings


fun Context.goToSecondary(screen: CurrentScreen){
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
    Settings.System.putInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, enable)
}

fun enableDND(context: Context, enabled: Boolean) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (enabled) notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)
    else notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
}

fun enableBluetooth(enabled: Boolean) {
    val adapter = BluetoothAdapter.getDefaultAdapter()
    if (enabled) adapter.enable() else adapter.disable()
}

fun enableDarkMode(context: Context, enabled: Boolean) {
    val manager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    manager.nightMode = if (enabled) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO
}

fun Context.enableFlashlight(enabled: Boolean) {
    val cameraManager = getSystemService(ControlsProviderService.CAMERA_SERVICE) as CameraManager
    val cameraId = cameraManager.cameraIdList[0]
    cameraManager.setTorchMode(cameraId, enabled)
}