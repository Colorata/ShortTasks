package com.colorata.st.extensions

import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.res.Configuration
import android.location.LocationManager
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.colorata.st.ui.theme.Strings
import android.content.pm.PackageManager
import com.colorata.st.ui.theme.SuperStore


@Composable
fun getBottomNavigationHeight(): Dp {
    return SuperStore(LocalContext.current).catchInt(Strings.bottomSize, 30).dp
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

fun Context.isWifiEnabled(): Boolean {
    val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
    return wifiManager.isWifiEnabled
}

fun isBluetoothEnabled(): Boolean {
    val adapter = BluetoothAdapter.getDefaultAdapter()
    return adapter.isEnabled
}

fun Context.isLocationEnabled(): Boolean {
    val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}

fun Context.isMobileDataEnabled(): Boolean =
    Settings.Secure.getInt(contentResolver, "mobile_data", 1) == 1

fun Context.isBatterySaverEnabled(): Boolean {
    val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
    return powerManager.isPowerSaveMode
}

fun Context.getBrightness(): Int {
    Settings.System.putInt(
        contentResolver,
        Settings.System.SCREEN_BRIGHTNESS_MODE,
        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
    )

    return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
}

fun Context.getMediaVolume(): Float {
    val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager?
    val max = audioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
    return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / (max / 100)
}

fun Context.getRingVolume(): Float {
    val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager?
    val max = audioManager!!.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION).toFloat()
    return audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) / (max / 100)
}

fun Context.isAutoRotationEnabled(): Boolean {
    val enabled = Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION)
    return enabled == 1
}

fun Context.isDNDEnabled(): Boolean {
    val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager.currentInterruptionFilter == NotificationManager.INTERRUPTION_FILTER_PRIORITY
}

fun Context.isDarkThemeEnabled(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun Context.isAirplaneEnabled(): Boolean {
    return Settings.System.getInt(
        contentResolver,
        Settings.Global.AIRPLANE_MODE_ON, 0
    ) != 0
}

fun Context.isPackageInstalled(name: String): Boolean {
    return try {
        packageManager.getPackageInfo(name, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}