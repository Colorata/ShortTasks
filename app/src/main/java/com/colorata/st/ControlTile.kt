package com.colorata.st

import android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_POWER_DIALOG
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.hardware.camera2.CameraManager
import android.provider.Settings
import android.service.controls.Control
import android.service.controls.ControlsProviderService
import android.service.controls.DeviceTypes
import android.service.controls.actions.BooleanAction
import android.service.controls.actions.ControlAction
import android.service.controls.templates.ControlButton
import android.service.controls.templates.ToggleTemplate
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.lang.reflect.Method
import java.util.concurrent.Flow
import java.util.function.Consumer


@RequiresApi(30)
class ControlTile : ControlsProviderService() {

    @SuppressLint("WrongConstant")
    fun addControls(name: String, title: String = name, @DrawableRes icon: Int, enabled: Boolean = true) =
        Control.StatefulBuilder(
            name,
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        ).setTitle(title)
            .setSubtitle("Tap to open")
            .setDeviceType(DeviceTypes.TYPE_GENERIC_ON_OFF)
            .setCustomIcon(
                Icon.createWithResource(
                    this,
                    icon
                )
            )
            .setControlId(name)
            .setStatus(1)
            .setControlTemplate(ToggleTemplate("button", ControlButton(enabled, "button"))).build()

    private var flashlightOn = false

    private val controlList: List<Control>
        @SuppressLint("WrongConstant")
        get() {

            return mutableListOf(
                addControls(name = "Search", icon = R.drawable.ic_baseline_search_24),
                addControls("Tethering", "HotSpot and Tethering", R.drawable.ic_baseline_wifi_tethering_24, true),
                addControls("WiFi", "Wi-Fi", R.drawable.ic_baseline_wifi_24, true),
                addControls("Flashlight", "Flashlight", R.drawable.ic_baseline_flash_on_24, false),
                addControls("Bluetooth", "Bluetooth", R.drawable.ic_baseline_bluetooth_24, true),
                addControls("MobData", "Mobile Data", R.drawable.ic_baseline_data_usage_24, true),
                addControls("NearShare", "Nearby Sharing", R.drawable.ic_baseline_share_24, true),
                addControls("Location", "Location", R.drawable.ic_baseline_location_on_24, true),
                addControls("Calc", "Calculator", R.drawable.ic_baseline_calculate_24, true),
                addControls("BatSave", "Battery Saver", R.drawable.ic_baseline_battery_full_24, true),
                addControls("Tasks", "Google Tasks", R.drawable.ic_baseline_add_task_24, true),
                addControls("Notify", "Notifications", R.drawable.ic_baseline_notifications_24, true)
            )

        }

    override fun createPublisherForAllAvailable(): Flow.Publisher<Control> {
        return Flow.Publisher {
            for (control in controlList) {
                it.onNext(control)
            }
            it.onComplete()
        }
    }

    @SuppressLint("WrongConstant")
    override fun performControlAction(
        controlId: String,
        action: ControlAction,
        consumer: Consumer<Int>
    ) {
        /*val component = ComponentName(applicationContext, AccessibilityService::class.java)
        applicationContext.packageManager.setComponentEnabledSetting(
            component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )*/

        when (controlId) {
            "Search" -> {
                val i = Intent()
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.setClassName(
                    "com.google.android.googlequicksearchbox",
                    "com.google.android.apps.gsa.search_gesture.GestureActivity"
                )
                startActivity(i)

                val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                consumer.accept(ControlAction.RESPONSE_OK)
            }
            "Tethering" -> {
                val i = Intent()
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.setClassName("com.android.settings", "com.android.settings.TetherSettings")
                startActivity(i)

                val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                consumer.accept(ControlAction.RESPONSE_OK)
            }
            "WiFi" -> {
                val i = Intent(Settings.ACTION_WIFI_SETTINGS)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)

                val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                consumer.accept(ControlAction.RESPONSE_OK)
            }
            "Flashlight" -> {
                if (action is BooleanAction) {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    action.newState
                }
                val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
                if (!flashlightOn) {
                    flashlightOn = !flashlightOn
                    val cameraId = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(cameraId, flashlightOn)
                } else if (flashlightOn) {
                    flashlightOn = !flashlightOn
                    val cameraId = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(cameraId, flashlightOn)
                }
                Log.d(
                    "ControlTest",
                    "performControlAction $controlId $action $consumer"
                )
            }
            "Bluetooth" -> {
                val i = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)

                val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                consumer.accept(ControlAction.RESPONSE_OK)
            }
            "MobData" -> {
                val i = Intent(Settings.ACTION_DATA_USAGE_SETTINGS)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)

                val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                consumer.accept(ControlAction.RESPONSE_OK)
            }
            "NearShare" -> {
                val i = Intent()
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.setClassName(
                    "com.google.android.gms",
                    "com.google.android.gms.nearby.sharing.ReceiveSurfaceActivity"
                )
                startActivity(i)

                val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                consumer.accept(ControlAction.RESPONSE_OK)
            }
            "Location" -> {
                val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)

                val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                consumer.accept(ControlAction.RESPONSE_OK)
            }
            "Calc" -> {
                val i = Intent()
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.setClassName(
                    "com.google.android.calculator",
                    "com.android.calculator2.Calculator"
                )
                startActivity(i)

                val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                consumer.accept(ControlAction.RESPONSE_OK)
            }
            "BatSave" -> {
                val i = Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)

                val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                consumer.accept(ControlAction.RESPONSE_OK)
            }
            "Tasks" -> {
                val i = Intent()
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.setClassName(
                    "com.google.android.apps.tasks",
                    "com.google.android.apps.tasks.ui.TaskListsActivity"
                )
                startActivity(i)

                val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                consumer.accept(ControlAction.RESPONSE_OK)
            }
            "Notify" -> {
                val sbservice = getSystemService("statusbar")
                val statusbarManager = Class.forName("android.app.StatusBarManager")
                val showsb: Method = statusbarManager.getMethod("expandNotificationsPanel")
                showsb.invoke(sbservice)

                val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                consumer.accept(ControlAction.RESPONSE_OK)
            }
        }


    }

    override fun createPublisherFor(controlIds: MutableList<String>): Flow.Publisher<Control> {
        Log.d("ControlTest", "publisherFor $controlIds")
        return Flow.Publisher {
            for (control in controlList) {
                if (controlIds.contains(control.controlId)) {
                    Log.d("ControlTest", "Found ${control.controlId}")
                    it.onSubscribe(object : Flow.Subscription {
                        override fun cancel() {
                            Log.d("ControlTest", "cancel")
                        }

                        override fun request(p0: Long) {
                            Log.d("ControlTest", "request $p0")
                        }

                    })
                    it.onNext(control)
                }
            }
        }
    }

    /*private fun ArrayList<Control>.add(inline: () -> Control) {
        this.add(inline.invoke())
    }*/

}