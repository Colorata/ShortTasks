package com.colorata.st.ui.theme

import android.content.Intent
import android.provider.Settings
import com.colorata.st.R

enum class Controls(
    val id: Int,
    val title: String,
    val subTitle: String = "",
    val icon: Int,
    val isRange: Boolean,
    val intent: Intent = Intent(Settings.ACTION_SETTINGS)
) {
    SEARCH(
        id = 1500,
        title = Strings.search,
        icon = R.drawable.ic_outline_search_24,
        subTitle = Strings.tap,
        isRange = false
    ),
    HOTSPOT(
        id = 1501,
        title = Strings.tethering,
        icon = R.drawable.ic_outline_wifi_tethering_24,
        subTitle = Strings.hold,
        isRange = false,
        intent = Intent().setClassName(
            "com.android.settings",
            "com.android.settings.TetherSettings"
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    WIFI(
        id = 1502,
        title = Strings.wifi,
        icon = R.drawable.ic_outline_network_wifi_24,
        subTitle = Strings.hold,
        isRange = false,
        intent = Intent(Settings.ACTION_WIFI_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    FLASHLIGHT(
        id = 1503,
        title = Strings.flashlight,
        icon = R.drawable.ic_outline_flash_on_24,
        subTitle = "Tap",
        isRange = false
    ),
    BLUETOOTH(
        id = 1504,
        title = Strings.bluetooth,
        icon = R.drawable.ic_outline_bluetooth_24,
        subTitle = Strings.tap,
        isRange = false,
        intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    MOBILE_DATA(
        id = 1505,
        title = Strings.mobData,
        icon = R.drawable.ic_outline_network_cell_24,
        subTitle = Strings.hold,
        isRange = false,
        intent = Intent(Settings.ACTION_DATA_USAGE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    NEARBY_SHARING(
        id = 1506,
        title = Strings.nearShare,
        icon = R.drawable.ic_outline_share_24,
        subTitle = Strings.hold,
        isRange = false,
        intent = Intent().setClassName(
            "com.google.android.gms",
            "com.google.android.gms.nearby.sharing.ReceiveSurfaceActivity"
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    LOCATION(
        id = 1507,
        title = Strings.location,
        icon = R.drawable.ic_outline_location_on_24,
        subTitle = Strings.tap,
        isRange = false,
        intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    CALCULATOR(
        id = 1508,
        title = Strings.calc,
        icon = R.drawable.ic_outline_calculate_24,
        subTitle = Strings.hold,
        isRange = false,
        intent = Intent().setClassName(
            "com.google.android.calculator",
            "com.android.calculator2.Calculator"
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    BATTERY_INFO(
        id = 1509,
        title = Strings.battery,
        icon = R.drawable.ic_outline_battery_full_24,
        subTitle = Strings.hold,
        isRange = true,
        intent = Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    GOOGLE_TASKS(
        id = 1510,
        title = Strings.tasks,
        icon = R.drawable.ic_outline_add_task_24,
        subTitle = Strings.hold,
        isRange = false,
        intent = Intent().setClassName(
            "com.google.android.apps.tasks",
            "com.google.android.apps.tasks.ui.TaskListsActivity"
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    NOTIFICATIONS(
        id = 1511,
        title = Strings.notify,
        icon = R.drawable.ic_outline_announcement_24,
        subTitle = Strings.tap,
        isRange = false
    ),
    MEDIA_VOLUME(
        id = 1512,
        title = Strings.mediaVolume,
        icon = R.drawable.ic_outline_music_note_24,
        subTitle = Strings.slideOrTap,
        isRange = true,
        intent = Intent(Settings.ACTION_SOUND_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    RING_VOLUME(
        id = 1513,
        title = Strings.ringVolume,
        icon = R.drawable.ic_outline_circle_notifications_24,
        subTitle = Strings.slideOrTap,
        isRange = true,
        intent = Intent(Settings.ACTION_SOUND_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    BRIGHTNESS(
        id = 1514,
        title = Strings.brightness,
        icon = R.drawable.ic_outline_brightness_7_24,
        subTitle = Strings.slideOrTap,
        isRange = true,
        intent = Intent(Settings.ACTION_DISPLAY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    AUTO_ROTATE(
        id = 1515,
        title = Strings.autoRotate,
        icon = R.drawable.ic_outline_screen_rotation_24,
        subTitle = Strings.tap,
        isRange = false,
        intent = Intent(Settings.ACTION_DISPLAY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    DND(
        id = 1516,
        title = Strings.dnd,
        icon = R.drawable.ic_outline_do_disturb_on_24,
        subTitle = Strings.hold,
        isRange = false,
        intent = Intent(Settings.ACTION_CONDITION_PROVIDER_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    NIGHT_LIGHT(
        id = 1517,
        title = Strings.nightLight,
        icon = R.drawable.ic_outline_nightlight_24,
        subTitle = Strings.hold,
        isRange = false,
        intent = Intent(Settings.ACTION_ZEN_MODE_PRIORITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    FLIGHT_MODE(
        id = 1518,
        title = Strings.flightMode,
        icon = R.drawable.ic_outline_flight_24,
        subTitle = Strings.hold,
        isRange = false,
        intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    ),
    TIME(
        id = 1520,
        title = Strings.time,
        subTitle = Strings.hold,
        icon = R.drawable.ic_outline_access_time_24,
        isRange = true,
        intent = Intent(Settings.ACTION_DATE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    )
}