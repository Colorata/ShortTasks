package com.colorata.st.extensions

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
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.colorata.st.R
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.SuperStore
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

fun Context.isMobileDataEnabled(): Boolean =
    Settings.Secure.getInt(contentResolver, "mobile_data", 1) == 1

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
    return Settings.System.getInt(
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
    if (isWifiEnabled()) R.drawable.ic_outline_network_wifi_24
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

fun Context.getCurrentRingVolumeIcon(): Int =
    if (getRingVolume() == 0f) R.drawable.ic_outline_notifications_off_24
    else R.drawable.ic_outline_notifications_none_24

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
    if (isAutoRotationEnabled()) R.drawable.ic_outline_screen_rotation_24 else R.drawable.ic_outline_screen_lock_rotation_24

fun Context.getCurrentDNDIcon(): Int =
    if (isDNDEnabled()) R.drawable.ic_outline_do_disturb_on_24 else R.drawable.ic_outline_do_not_disturb_off_24

fun Context.isMicrophoneEnabled(): Boolean {
    val manager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    return !manager.isMicrophoneMute
}