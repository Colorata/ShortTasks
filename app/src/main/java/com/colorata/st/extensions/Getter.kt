package com.colorata.st.extensions

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.TrafficStats
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.PowerManager
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.colorata.st.R
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.SuperStore
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.TimeUnit


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

fun Context.isMobileDataEnabled(): Boolean {
    var mobileDataEnabled = false // Assume disabled

    val cm = getSystemService(Context.CONNECTIVITY_SERVICE)
    return try {
        val cmClass = Class.forName(cm.javaClass.name)
        val method = cmClass.getDeclaredMethod("getMobileDataEnabled")
        method.isAccessible = true
        mobileDataEnabled = method.invoke(cm) as Boolean
        mobileDataEnabled
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}


fun Context.isBatterySaverEnabled(): Boolean {
    val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
    return powerManager.isPowerSaveMode
}

fun Context.getBrightness(): Int {
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
    return Settings.Secure.getInt(
        contentResolver,
        Settings.Global.AIRPLANE_MODE_ON, 0
    ) != 0
}

fun Context.isAutoBrightnessEnabled(): Boolean {
    return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE) == 1
}

fun Context.isPackageInstalled(name: String): Boolean {
    return try {
        packageManager.getPackageInfo(name, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Context.getBatteryPercentage(): Int {
    val manager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager

    return manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
}

fun getTime(): Pair<String, String> {
    val time = Calendar.getInstance().time.toString()
    val first = "${time[11]}${time[12]}"
    val second = "${time[14]}${time[15]}"
    return Pair(first, second)
}

fun getDate(): String {
    val time = Calendar.getInstance().time.toString()
    val day = time.substring(0..2)
    val date = time.substring(4..9)
    return "$day, $date"
}

fun Context.getChargingTimeRemaining(): Pair<String, String> {
    val manager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    val duration = manager.computeChargeTimeRemaining()
    val hoursRemaining = TimeUnit.MILLISECONDS.toHours(duration)
    val minutesRemaining = TimeUnit.MILLISECONDS.toMinutes(duration) - hoursRemaining * 60
    val currentTime = getTime()
    var estimatedHour = 0L
    val estimatedMinute: Long
    if (currentTime.second.toInt() + minutesRemaining > 60) {
        estimatedMinute = currentTime.second.toInt() + minutesRemaining - 60
        estimatedHour = 1L
    } else {
        estimatedMinute = currentTime.second.toInt() + minutesRemaining
    }
    estimatedHour +=
        if (currentTime.first.toInt() + hoursRemaining >= 24) currentTime.first.toInt() + hoursRemaining - 24
        else currentTime.first.toInt() + hoursRemaining
    val finalMinute =
        if (estimatedMinute == 0L) "00" else if (estimatedMinute < 10) "0$estimatedMinute" else estimatedMinute.toString()
    return Pair(estimatedHour.toString(), finalMinute)
}

fun Context.getCurrentBatteryIcon(): Int {
    val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
        applicationContext.registerReceiver(null, ifilter)
    }
    val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
    val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
            || status == BatteryManager.BATTERY_STATUS_FULL

    return when {
        isCharging -> R.drawable.ic_outline_battery_charging_full_24
        isBatterySaverEnabled() -> R.drawable.ic_outline_battery_saver_24
        getBatteryPercentage() < 20 -> R.drawable.ic_outline_battery_alert_24
        else -> R.drawable.ic_outline_battery_full_24
    }
}

fun Context.getBatteryFormat(): String {
    val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
        applicationContext.registerReceiver(null, ifilter)
    }
    val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
    val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
            || status == BatteryManager.BATTERY_STATUS_FULL

    return if (isCharging && getChargingTimeRemaining() == getTime()) "${Strings.percentFormat} ${Strings.dotIcon} Charging"
    else if (isCharging) "${Strings.percentFormat} ${Strings.dotIcon} Full in " +
            "${getChargingTimeRemaining().first}:${getChargingTimeRemaining().second}"
    else if (!isCharging && getBatteryPercentage() > 20) "${Strings.percentFormat} ${Strings.dotIcon} Discharging"
    else if (!isCharging && getBatteryPercentage() <= 20) "${Strings.percentFormat} ${Strings.dotIcon} Low battery"
    else Strings.percentFormat
}

fun getCurrentBluetoothIcon(): Int =
    if (isBluetoothEnabled()) R.drawable.ic_outline_bluetooth_24
    else R.drawable.ic_outline_bluetooth_disabled_24

fun Context.getCurrentWifiIcon(): Int =
    if (isWifiEnabled()) R.drawable.ic_outline_signal_wifi_4_bar_24
    else R.drawable.ic_outline_signal_wifi_off_24

fun Context.getCurrentFlashlightIcon(): Int =
    if (isFlashlightEnabled()) R.drawable.ic_outline_flash_on_24
    else R.drawable.ic_outline_flash_off_24

fun Context.getCurrentBrightnessIcon(): Int =
    when {
        isAutoBrightnessEnabled() -> R.drawable.ic_outline_brightness_auto_24
        getBrightness() > 75 -> R.drawable.ic_outline_brightness_7_24
        getBrightness() in 26..75 -> R.drawable.ic_outline_brightness_medium_24
        getBrightness() <= 25 -> R.drawable.ic_outline_brightness_low_24
        else -> R.drawable.ic_outline_brightness_7_24
    }

fun Context.isFlashlightEnabled(): Boolean = SuperStore(this).catchBoolean(Strings.flashlight)

fun Context.getCurrentBrightnessFormat(): String =
    if (isAutoBrightnessEnabled()) "${Strings.percentFormat} ${Strings.dotIcon} Adaptive" else "${Strings.percentFormat} ${Strings.dotIcon} Manual"

fun Context.getCurrentMediaVolumeIcon(): Int =
    if (getMediaVolume() == 0f) R.drawable.ic_outline_music_off_24
    else R.drawable.ic_outline_music_note_24

fun Context.getCurrentRingVolumeIcon(): Int {
    val manager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    return when (manager.ringerMode) {
        AudioManager.RINGER_MODE_NORMAL -> R.drawable.ic_outline_notifications_none_24
        AudioManager.RINGER_MODE_SILENT -> R.drawable.ic_outline_notifications_off_24
        AudioManager.RINGER_MODE_VIBRATE -> R.drawable.ic_outline_vibration_24
        else -> R.drawable.ic_outline_notifications_none_24
    }
}

fun Context.getTimeFormat(): String {
    val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    if (manager.nextAlarmClock == null) {
        return "${getTime().first}:${getTime().second} ${Strings.dotIcon} No Alarms"
    }
    val time = Date(manager.nextAlarmClock.triggerTime)
    val calendar = Calendar.getInstance()
    calendar.time = time
    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val minutes = calendar.get(Calendar.MINUTE)
    val finalMinutes =
        if (minutes == 0) "00" else if (minutes < 10L) "0${minutes}" else minutes.toString()
    val finalHours = hours.toString()
    return "${getTime().first}:${getTime().second} ${Strings.dotIcon} Alarm ${finalHours}:${finalMinutes}"
}

fun Context.getCurrentLocationIcon(): Int =
    if (isLocationEnabled()) R.drawable.ic_outline_location_on_24
    else R.drawable.ic_outline_location_off_24

fun Context.getCurrentAutoRotationIcon(): Int =
    if (isAutoRotationEnabled()) R.drawable.ic_outline_screen_rotation_24
    else R.drawable.ic_outline_screen_lock_rotation_24

fun Context.getCurrentDNDIcon(): Int =
    if (isDNDEnabled()) R.drawable.ic_outline_do_disturb_on_24
    else R.drawable.ic_outline_do_not_disturb_off_24

fun Context.isMicrophoneEnabled(): Boolean {
    val manager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    return !manager.isMicrophoneMute
}

fun Context.getCurrentMicrophoneIcon(): Int =
    if (isMicrophoneEnabled()) R.drawable.ic_outline_mic_none_24
    else R.drawable.ic_outline_mic_off_24

fun Context.getCurrentAirplaneIcon(): Int =
    if (isAirplaneEnabled()) R.drawable.ic_outline_flight_24
    else R.drawable.ic_outline_airplanemode_inactive_24

fun Context.getMediaVolumeFormat(): String {
    val manager = getSystemService(Context.MEDIA_ROUTER_SERVICE) as MediaRouter
    val output = manager.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_AUDIO).name
    return "${Strings.percentFormat} ${Strings.dotIcon} $output"
}

fun Context.getRingVolumeFormat(): String {
    val manager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    return when (manager.ringerMode) {
        AudioManager.RINGER_MODE_NORMAL -> "${Strings.percentFormat} ${Strings.dotIcon} Ring"
        AudioManager.RINGER_MODE_SILENT -> "${Strings.percentFormat} ${Strings.dotIcon} Mute"
        AudioManager.RINGER_MODE_VIBRATE -> "${Strings.percentFormat} ${Strings.dotIcon} Vibrate"
        else -> Strings.percentFormat
    }
}

@SuppressLint("MissingPermission")
fun Context.getMobileDataQuality(): String {
    val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return when (manager.dataNetworkType) {
        TelephonyManager.NETWORK_TYPE_GPRS,
        TelephonyManager.NETWORK_TYPE_EDGE,
        TelephonyManager.NETWORK_TYPE_CDMA,
        TelephonyManager.NETWORK_TYPE_1xRTT,
        TelephonyManager.NETWORK_TYPE_IDEN,
        TelephonyManager.NETWORK_TYPE_GSM
        -> "E"
        TelephonyManager.NETWORK_TYPE_UMTS,
        TelephonyManager.NETWORK_TYPE_EVDO_0,
        TelephonyManager.NETWORK_TYPE_EVDO_A,
        TelephonyManager.NETWORK_TYPE_HSDPA,
        TelephonyManager.NETWORK_TYPE_HSUPA,
        TelephonyManager.NETWORK_TYPE_HSPA,
        TelephonyManager.NETWORK_TYPE_EVDO_B,
        TelephonyManager.NETWORK_TYPE_EHRPD,
        TelephonyManager.NETWORK_TYPE_HSPAP,
        TelephonyManager.NETWORK_TYPE_TD_SCDMA
        -> "3G"
        TelephonyManager.NETWORK_TYPE_LTE
        -> "LTE"
        TelephonyManager.NETWORK_TYPE_NR
        -> "5G"
        else -> "Unknown"
    }
}

fun Context.getWIFIQuality(): String {
    val manager = getSystemService(Context.WIFI_SERVICE) as WifiManager
    val rssi = manager.connectionInfo.rssi
    return if (!isWifiEnabled()) "Off" else if (!isWifiConnected()) "Not Connected"
    else if (rssi > -50) "Excellent"
    else if (rssi in -50 downTo -60) "Good"
    else if (rssi in -60 downTo -70) "Fair"
    else "Weak"
}

fun Context.getMobileDataFormat(): String {
    return if (isMobileDataEnabled()) "${getMobileDataQuality()} ${Strings.dotIcon} ${
        TrafficStats.getMobileRxBytes().toData()
    }" else "Off"
}

fun Context.getWIFIFormat(): String {
    return "${getWIFIQuality()} ${Strings.dotIcon} ${(TrafficStats.getTotalRxBytes() - TrafficStats.getMobileRxBytes()).toData()}"
}

fun Context.isWifiConnected() =
    (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
        getNetworkCapabilities(activeNetwork)?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            ?: false
    }

fun Context.isHotSpotEnabled(): Boolean {
    val manager = getSystemService(Context.WIFI_SERVICE) as WifiManager
    try {
        val method: Method = manager.javaClass.getDeclaredMethod("isWifiApEnabled")
        method.isAccessible = true
        return method.invoke(manager) as Boolean
    } catch (ignored: Throwable) { }
    return false
}

fun Context.isMusicPlaying(): Boolean {
    val manager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    return manager.isMusicActive
}

fun Context.getCurrentPlayerFormat(): String {
    return if (isMusicPlaying()) {
        "Playing"
    } else "Paused"
}

fun getCurrentPlayerIcon(event: String): Int {
    return when (event) {
        "Previous" -> R.drawable.ic_outline_skip_previous_24
        "Next" -> R.drawable.ic_outline_skip_next_24
        "Playing" -> R.drawable.ic_outline_play_arrow_24
        else -> R.drawable.ic_outline_pause_24
    }
}