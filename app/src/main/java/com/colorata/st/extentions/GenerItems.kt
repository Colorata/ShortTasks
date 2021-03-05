package com.colorata.st.extentions

import com.colorata.st.R

class GenerItems {

    var icons: MutableList<Int> = mutableListOf()
    var names: MutableList<String> = mutableListOf()

    fun icons(): MutableList<Int>{
        val icons = mutableListOf<Int>()
        icons.add(R.drawable.ic_baseline_search_24) // 0
        icons.add(R.drawable.ic_baseline_wifi_tethering_24) // 1
        icons.add(R.drawable.ic_baseline_wifi_24) // 2
        icons.add(R.drawable.ic_baseline_flash_on_24) // 3
        icons.add(R.drawable.ic_baseline_bluetooth_24) // 4
        icons.add(R.drawable.ic_baseline_data_usage_24) // 5
        icons.add(R.drawable.ic_baseline_share_24) // 6
        icons.add(R.drawable.ic_baseline_location_on_24) // 7
        icons.add(R.drawable.ic_baseline_calculate_24) // 8
        icons.add(R.drawable.ic_baseline_battery_full_24) // 9
        icons.add(R.drawable.ic_baseline_add_task_24) // 10
        icons.add(R.drawable.ic_baseline_notifications_24) // 11
        icons.add(R.drawable.ic_baseline_face_24) // user 12
        return icons
    }

    fun names():MutableList<String>{
        val values = mutableListOf<String>()
        values.add("Search") // 0
        values.add("Tethering") // 1
        values.add("WiFi") // 2
        values.add("Flashlight") // 3
        values.add("Bluetooth") // 4
        values.add("MobData") // 5
        values.add("Nearby Sharing") // 6
        values.add("Location") // 7
        values.add("Calculator") // 8
        values.add("Battery Saver") // 9
        values.add("Google Tasks") // 10
        values.add("Notifications") // 11
        //values.add(getUserTitle()) // user 12
        return values
    }
}