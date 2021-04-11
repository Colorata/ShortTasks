package com.colorata.st.extensions

import com.colorata.st.R
import com.colorata.st.ui.theme.Strings

fun defaultTitles(): List<String>{
    val values = mutableListOf<String>()
    values.add(Strings.search) // 0
    values.add(Strings.tethering) // 1
    values.add(Strings.wifi) // 2
    values.add(Strings.flashlight) // 3
    values.add(Strings.bluetooth) // 4
    values.add(Strings.mobData) // 5
    values.add(Strings.nearShare) // 6
    values.add(Strings.location) // 7
    values.add(Strings.calc) // 8
    values.add(Strings.batSave) // 9
    values.add(Strings.tasks) // 10
    values.add(Strings.notify) // 11
    return values

}

fun defaultIcons(): List<Int>{
    val icons = mutableListOf<Int>()
    icons.add(R.drawable.ic_outline_search_24) // 0
    icons.add(R.drawable.ic_outline_wifi_tethering_24) // 1
    icons.add(R.drawable.ic_outline_network_wifi_24) // 2
    icons.add(R.drawable.ic_outline_flash_on_24) // 3
    icons.add(R.drawable.ic_outline_bluetooth_24) // 4
    icons.add(R.drawable.ic_outline_network_cell_24) // 5
    icons.add(R.drawable.ic_outline_share_24) // 6
    icons.add(R.drawable.ic_outline_location_on_24) // 7
    icons.add(R.drawable.ic_outline_calculate_24) // 8
    icons.add(R.drawable.ic_outline_power_settings_new_24) // 9
    icons.add(R.drawable.ic_outline_add_task_24) // 10
    icons.add(R.drawable.ic_outline_circle_notifications_24) // 11
    icons.add(R.drawable.ic_outline_info_24) // user 12
    return icons

}