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
import android.service.controls.actions.ControlAction
import android.service.controls.templates.ControlButton
import android.service.controls.templates.ToggleTemplate
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.colorata.st.activities.MainActivity
import com.colorata.st.ui.theme.Strings
import io.reactivex.processors.ReplayProcessor
import java.lang.reflect.Method
import java.util.concurrent.Flow
import java.util.function.Consumer


@RequiresApi(30)
class PowerAssistant : ControlsProviderService() {

    private val controlFlows =
        mutableMapOf<String, ReplayProcessor<Control>>()

    //Fun for adding CONTROLS IN POWER ASSISTANT
    @SuppressLint("WrongConstant", "UnspecifiedImmutableFlag")
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
            .setControlTemplate(ToggleTemplate(
                Strings.button,
                ControlButton(enabled, Strings.button)
            )).build()

    //Init FLASHLIGHT STATE
    private var flashlightOn = false

    //Creating LIST of CONTROLS
    private val controlList: List<Control>
        get() {

            return mutableListOf(
                addControls(name = Strings.search, icon = R.drawable.ic_outline_search_24),
                addControls(name = Strings.tethering, icon = R.drawable.ic_outline_wifi_tethering_24),
                addControls(name = Strings.wifi, icon = R.drawable.ic_outline_network_wifi_24),
                addControls(name = Strings.flashlight, icon = R.drawable.ic_outline_flash_on_24),
                addControls(name = Strings.bluetooth, icon = R.drawable.ic_outline_bluetooth_24),
                addControls(name = Strings.mobData, icon = R.drawable.ic_outline_network_cell_24),
                addControls(name = Strings.nearShare, icon = R.drawable.ic_outline_share_24),
                addControls(name = Strings.location, icon = R.drawable.ic_outline_location_on_24),
                addControls(name = Strings.calc, icon = R.drawable.ic_outline_calculate_24),
                addControls(name = Strings.batSave, icon = R.drawable.ic_outline_power_settings_new_24),
                addControls(name = Strings.tasks, icon = R.drawable.ic_outline_add_task_24),
                addControls(name = Strings.notify, icon = R.drawable.ic_outline_announcement_24)
            )

        }

    //Fun for SHOWING CONTROLS
    override fun createPublisherForAllAvailable(): Flow.Publisher<Control> {
        return Flow.Publisher {
            for (control in controlList) {
                it.onNext(control)
            }
            it.onComplete()
        }
    }

    //Fun for CLICK LISTENER for CONTROLS
    @SuppressLint("WrongConstant")
    override fun performControlAction(
        controlId: String,
        action: ControlAction,
        consumer: Consumer<Int>
    ) {
        //Component for DON'T KILLING SERVICE
        /*val component = ComponentName(applicationContext, AccessibilityService::class.java)
        applicationContext.packageManager.setComponentEnabledSetting(
            component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )*/

        consumer.accept(ControlAction.RESPONSE_OK)
        controlFlows[controlId]?.let { flow ->

            when (controlId) {
                Strings.search -> {

                    //Going to GOOGLE SEARCH
                    val i = Intent()
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.setClassName(
                        "com.google.android.googlequicksearchbox",
                        "com.google.android.apps.gsa.search_gesture.GestureActivity"
                    )
                    startActivity(i)

                    //Power Assistant HIDING
                    val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                    intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
                Strings.tethering -> {

                    //Going to TETHERING SETTINGS
                    val i = Intent()
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.setClassName("com.android.settings", "com.android.settings.TetherSettings")
                    startActivity(i)

                    //Power Assistant HIDING
                    val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                    intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
                Strings.wifi -> {

                    //Going to WIFI SETTINGS
                    val i = Intent(Settings.ACTION_WIFI_SETTINGS)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)

                    //Power Assistant HIDING
                    val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                    intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
                Strings.flashlight -> {

                    //Changing FLASHLIGHT STATE
                    val cameraManager = getSystemService(CameraManager::class.java)
                    flashlightOn = !flashlightOn
                    val cameraId = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(cameraId, flashlightOn)

                    flow.onNext(addControls(name = Strings.search, icon = R.drawable.ic_outline_search_24, enabled = true))
                }
                Strings.bluetooth -> {

                    //Going to BLUETOOTH SETTINGS
                    val i = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)

                    //Power Assistant HIDING
                    val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                    intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
                Strings.mobData -> {

                    //Going to DATA USAGE SETTINGS
                    val i = Intent(Settings.ACTION_DATA_USAGE_SETTINGS)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)

                    //Power Assistant HIDING
                    val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                    intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
                Strings.nearShare -> {

                    //Going to NEARBY SHARING PAGE
                    val i = Intent()
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.setClassName(
                        "com.google.android.gms",
                        "com.google.android.gms.nearby.sharing.ReceiveSurfaceActivity"
                    )
                    startActivity(i)

                    //Power Assistant HIDING
                    val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                    intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
                Strings.location -> {

                    //Going to LOCATION SETTINGS
                    val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)

                    //Power Assistant HIDING
                    val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                    intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
                Strings.calc -> {

                    //Going to GOOGLE CALCULATOR
                    val i = Intent()
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.setClassName(
                        "com.google.android.calculator",
                        "com.android.calculator2.Calculator"
                    )
                    startActivity(i)

                    //Power Assistant HIDING
                    val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                    intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
                Strings.batSave -> {

                    //Going to BATTERY SAVER
                    val i = Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)

                    //Power Assistant HIDING
                    val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                    intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
                Strings.tasks -> {

                    //Going to GOOGLE TASKS
                    val i = Intent()
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.setClassName(
                        "com.google.android.apps.tasks",
                        "com.google.android.apps.tasks.ui.TaskListsActivity"
                    )
                    startActivity(i)

                    //Power Assistant HIDING
                    val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                    intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
                Strings.notify -> {

                    //Showing NOTIFICATIONS
                    val sbservice = getSystemService("statusbar")
                    val statusbarManager = Class.forName("android.app.StatusBarManager")
                    val showsb: Method = statusbarManager.getMethod("expandNotificationsPanel")
                    showsb.invoke(sbservice)

                    //Power Assistant HIDING
                    val intent = Intent("com.colorata.st.ACCESSIBILITY_ACTION")
                    intent.putExtra("action", GLOBAL_ACTION_POWER_DIALOG)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
                else -> let {  }
            }

        }

    }

    override fun createPublisherFor(controlIds: MutableList<String>): Flow.Publisher<Control> {
        return Flow.Publisher {
            for (control in controlList) {
                if (controlIds.contains(control.controlId)) {
                    it.onSubscribe(object : Flow.Subscription {
                        override fun cancel() {
                        }

                        override fun request(p0: Long) {
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