package com.colorata.st.ui.theme

import com.colorata.st.R

enum class Controls(
    id: Int,
    title: String,
    subTitle: String = "",
    enabled: Boolean = false,
    icon: Int,
    isRange: Boolean
) {
    SEARCH(id = 1500, title = Strings.search, icon = R.drawable.ic_outline_search_24, enabled = true, isRange = false),
    HOTSPOT(id = 1501, title = Strings.tethering, icon = R.drawable.ic_outline_wifi_tethering_24, isRange = false),
    WIFI(id = 1502, title = Strings.wifi, icon = R.drawable.ic_outline_network_wifi_24, isRange = false),
    FLASHLIGHT(id = 1503, title = Strings.flashlight, icon = R.drawable.ic_outline_flash_on_24, isRange = false),
    BLUETOOTH(id = 1504, title = Strings.bluetooth, icon = R.drawable.ic_outline_bluetooth_24, isRange = false),
    MOBILE_DATA(id = 1505, title = Strings.mobData, icon = R.drawable.ic_outline_network_cell_24, isRange = false),
    NEARBY_SHARING(id = 1506, title = Strings.nearShare, icon = R.drawable.ic_outline_share_24, enabled = true, isRange = false),
    LOCATION(id = 1507, title = Strings.location, icon = R.drawable.ic_outline_location_on_24, isRange = false),
    CALCULATOR(id = 1508, title = Strings.calc, icon = R.drawable.ic_outline_calculate_24, enabled = true, isRange = false),
    BATTERY_SAVER(id = 1509, title = Strings.batSave, icon = R.drawable.ic_outline_power_settings_new_24, isRange = false),
    GOOGLE_TASKS(id = 1510, title = Strings.tasks, icon = R.drawable.ic_outline_add_task_24, enabled = true, isRange = false),
    NOTIFICATIONS(id = 1511, title = Strings.notify, icon = R.drawable.ic_outline_announcement_24, enabled = true, isRange = false)
}