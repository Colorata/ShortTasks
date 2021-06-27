package com.colorata.st

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.net.Uri
import android.service.controls.Control
import android.service.controls.ControlsProviderService
import android.service.controls.DeviceTypes
import android.service.controls.actions.BooleanAction
import android.service.controls.actions.ControlAction
import android.service.controls.actions.FloatAction
import android.service.controls.templates.*
import android.util.Log
import com.colorata.st.extensions.*
import com.colorata.st.extensions.weather.WeatherResponse
import com.colorata.st.extensions.weather.WeatherService
import com.colorata.st.ui.theme.Controls
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.SuperStore
import com.colorata.st.ui.theme.backgroundIntControl
import io.reactivex.Flowable
import io.reactivex.processors.ReplayProcessor
import org.reactivestreams.FlowAdapters
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.Flow
import java.util.function.Consumer


class PowerControls : ControlsProviderService() {
    private var isRooted = false
    private val controlFlows =
        mutableMapOf<String, ReplayProcessor<Control>>()
    private var toggleState2 = false
    private var rangeState = 50f
    private var currentPlayerState = "Playing"
    private val controls: MutableList<Control>
        get() {
            isRooted = SuperStore(this).catchBoolean(Strings.hasRoot)
            val list = mutableListOf<Control>()
            //isRooted = SuperStore(this).catchBoolean(Strings.hasRoot)
            var minDegrees = SuperStore(this).catchInt(Strings.minDegrees, -50).toFloat()
            var maxDegrees = SuperStore(this).catchInt(Strings.maxDegrees, 50).toFloat()

            var currentRight = SuperStore(this).catchString("CurrentRightWeather")
            var currentFeels = SuperStore(this).catchString("CurrentFeelsWeather")
            var currentFloat = SuperStore(this).catchFloat("CurrentFloatWeather")

            val city = SuperStore(this).catchString(Strings.city, "Moscow")

            val retrofit = Retrofit.Builder()
                .baseUrl(Strings.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(WeatherService::class.java)
            val call = service.getCurrentWeatherData(city, "metric", Strings.appId)

            //CALLING
            call.enqueue(object : Callback<WeatherResponse> {

                //Fun if SUCCESS
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.code() == 200) {
                        val weatherResponse = response.body()!!

                        //Configuring TEXT
                        currentRight = weatherResponse.name?.replace("’", "")!!
                        currentFeels =
                            "Feels: " + weatherResponse.main!!.feels.toInt() + " \u2103"
                        currentFloat = weatherResponse.main!!.temp

                        SuperStore(this@PowerControls).drop(
                            mutableListOf(
                                Pair("CurrentRightWeather", currentRight as Any),
                                Pair("CurrentFeelsWeather", currentFeels as Any),
                                Pair("CurrentFloatWeather", currentFloat as Any)
                            )
                        )


                        list.add(
                            buildRangeControl(
                                id = 1441,
                                title = currentRight,
                                icon = R.drawable.ic_outline_cloud_24,
                                state = currentFloat,
                                subTitle = currentFeels,
                                isWeather = true,
                                minValue = -50f,
                                maxValue = 50f
                            )
                        )
                    }
                }

                //Fun if FAIL
                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    currentRight = "Error"
                    currentFeels = "Error"
                }
            })

            if (currentFloat < minDegrees) {
                minDegrees = currentFloat
                SuperStore(this).drop(Strings.minDegrees, minDegrees.toInt())
            } else if (currentFloat > maxDegrees) {
                maxDegrees = currentFloat
                SuperStore(this).drop(Strings.maxDegrees, maxDegrees.toInt())
            }

            list.clear()

            list.add(
                buildRangeControl(
                    id = 1441,
                    title = currentRight,
                    icon = R.drawable.ic_outline_cloud_24,
                    state = currentFloat,
                    subTitle = currentFeels,
                    isWeather = true,
                    minValue = minDegrees,
                    maxValue = maxDegrees
                )
            )

            Controls.values().forEach { control ->
                if (control.isRange) {
                    list.add(
                        buildRangeControl(
                            id = control.id,
                            title = if (control.id == Controls.TIME.id) getDate() else control.title,
                            icon = when (control.id) {
                                Controls.BATTERY_INFO.id -> getCurrentBatteryIcon()
                                Controls.BRIGHTNESS.id -> getCurrentBrightnessIcon()
                                Controls.MEDIA_VOLUME.id -> getCurrentMediaVolumeIcon()
                                Controls.RING_VOLUME.id -> getCurrentRingVolumeIcon()
                                else -> control.icon
                            },
                            state = when (control.id) {
                                Controls.BRIGHTNESS.id -> (getBrightness() / 2.55).toFloat()
                                Controls.RING_VOLUME.id -> getRingVolume()
                                Controls.MEDIA_VOLUME.id -> getMediaVolume()
                                Controls.BATTERY_INFO.id -> getBatteryPercentage().toFloat()
                                else -> 50f
                            },
                            intent = if (control.id == Controls.TIME.id && isPackageInstalled(
                                    Strings.googleClockApp
                                )
                            ) getAppIntent(Strings.googleClockApp) else control.intent,
                            time = if (control.id == Controls.TIME.id) getTime() else null,
                            format = when (control.id) {
                                Controls.BATTERY_INFO.id -> getBatteryFormat()
                                Controls.BRIGHTNESS.id -> getCurrentBrightnessFormat()
                                Controls.MEDIA_VOLUME.id -> getMediaVolumeFormat()
                                Controls.RING_VOLUME.id -> getRingVolumeFormat()
                                Controls.PLAYER.id -> getCurrentPlayerFormat()
                                else -> Strings.percentFormat
                            },
                            enabled = if (control.id == Controls.PLAYER.id) isMusicPlaying() else true
                        )
                    )
                } else {
                    when (control.id) {
                        Controls.SEARCH.id -> {
                            if (isPackageInstalled("com.google.android.googlequicksearchbox")) {
                                list.add(
                                    buildToggleControl(
                                        id = control.id,
                                        title = control.title,
                                        icon = control.icon,
                                        intent = control.intent
                                    )
                                )
                            }
                        }
                        Controls.NEARBY_SHARING.id -> {
                            if (isPackageInstalled("com.google.android.gms")) {
                                list.add(
                                    buildToggleControl(
                                        id = control.id,
                                        title = control.title,
                                        icon = control.icon,
                                        intent = control.intent
                                    )
                                )
                            }
                        }
                        Controls.CALCULATOR.id -> {
                            if (isPackageInstalled("com.google.android.calculator")) {
                                list.add(
                                    buildToggleControl(
                                        id = control.id,
                                        title = control.title,
                                        icon = control.icon,
                                        intent = control.intent
                                    )
                                )
                            }
                        }
                        Controls.GOOGLE_TASKS.id -> {
                            if (isPackageInstalled("com.google.android.apps.tasks")) {
                                list.add(
                                    buildToggleControl(
                                        id = control.id,
                                        title = control.title,
                                        icon = control.icon,
                                        intent = control.intent
                                    )
                                )
                            }
                        }
                        else -> {
                            list.add(
                                buildToggleControl(
                                    id = control.id,
                                    title = control.title,
                                    icon = when (control.id) {
                                        Controls.BLUETOOTH.id -> getCurrentBluetoothIcon()
                                        Controls.WIFI.id -> getCurrentWifiIcon()
                                        Controls.FLASHLIGHT.id -> getCurrentFlashlightIcon()
                                        Controls.LOCATION.id -> getCurrentLocationIcon()
                                        Controls.AUTO_ROTATE.id -> getCurrentAutoRotationIcon()
                                        Controls.DND.id -> getCurrentDNDIcon()
                                        Controls.MICROPHONE.id -> getCurrentMicrophoneIcon()
                                        Controls.FLIGHT_MODE.id -> getCurrentAirplaneIcon()
                                        else -> control.icon
                                    },
                                    enabled = when (control.id) {
                                        Controls.WIFI.id -> isWifiEnabled()
                                        Controls.BLUETOOTH.id -> isBluetoothEnabled()
                                        Controls.MOBILE_DATA.id -> isMobileDataEnabled()
                                        Controls.LOCATION.id -> isLocationEnabled()
                                        Controls.AUTO_ROTATE.id -> isAutoRotationEnabled()
                                        Controls.DND.id -> isDNDEnabled()
                                        Controls.DARK_THEME.id -> isDarkThemeEnabled()
                                        Controls.FLIGHT_MODE.id -> isAirplaneEnabled()
                                        Controls.FLASHLIGHT.id -> isFlashlightEnabled()
                                        Controls.MICROPHONE.id -> isMicrophoneEnabled()
                                        Controls.HOTSPOT.id -> isHotSpotEnabled()
                                        else -> false
                                    },
                                    intent = control.intent,
                                    stateText = when (control.id) {
                                        Controls.MOBILE_DATA.id -> getMobileDataFormat()
                                        Controls.WIFI.id -> getWIFIFormat()
                                        else -> ""
                                    }
                                )
                            )
                        }
                    }
                }
            }

            val title = SuperStore(this).catchString(Strings.linkTitle, "Google")
            val link = SuperStore(this).catchString(Strings.linkLink, "https://google.com")
            list.add(
                buildToggleControl(
                    id = 0,
                    title = title,
                    subTitle = Strings.link,
                    icon = R.drawable.ic_outline_android_24,
                    enabled = false,
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(link)
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            )
            return list
        }

    override fun createPublisherForAllAvailable(): Flow.Publisher<Control> =
        FlowAdapters.toFlowPublisher(
            Flowable.fromIterable(
                controls
            )
        )

    override fun createPublisherFor(controlIds: List<String>): Flow.Publisher<Control> {
        val flow: ReplayProcessor<Control> = ReplayProcessor.create(controlIds.size)

        controlIds.forEach { controlFlows[it] = flow }

        for (i in controls) {
            flow.onNext(i)
        }

        return FlowAdapters.toFlowPublisher(flow)
    }

    override fun performControlAction(
        controlId: String,
        action: ControlAction,
        consumer: Consumer<Int>
    ) {
        controlFlows[controlId]?.let { flow ->
            when (controlId) {

                0.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)

                    val link = SuperStore(this).catchString(Strings.linkLink, "https://google.com")
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(link)
                        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    hidePowerMenu()
                }
                1441.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    flow.onNext(controls[0])
                }

                Controls.SEARCH.id.toString() -> {
                    val i = Intent()
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .setClassName(
                            "com.google.android.googlequicksearchbox",
                            "com.google.android.apps.gsa.search_gesture.GestureActivity"
                        )
                    startActivity(i)
                    hidePowerMenu()
                }

                Controls.FLASHLIGHT.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState2 = action.newState
                    enableFlashlight(toggleState2)
                    Controls.FLASHLIGHT.also {
                        flow.onNext(
                            buildToggleControl(
                                id = it.id,
                                title = it.title,
                                enabled = isFlashlightEnabled(),
                                icon = getCurrentFlashlightIcon(),
                                intent = it.intent
                            )
                        )
                    }

                }
                Controls.BLUETOOTH.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState2 = action.newState
                    enableBluetooth(toggleState2)
                    Controls.BLUETOOTH.also {
                        flow.onNext(
                            buildToggleControl(
                                id = it.id,
                                title = it.title,
                                enabled = toggleState2,
                                icon = if (toggleState2) R.drawable.ic_outline_bluetooth_24 else R.drawable.ic_outline_bluetooth_disabled_24,
                                intent = it.intent
                            )
                        )
                    }
                }

                Controls.NOTIFICATIONS.id.toString() -> {
                    showNotifications()
                    hidePowerMenu()
                }

                Controls.MEDIA_VOLUME.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is FloatAction) {
                        rangeState = action.newValue
                        toggleState2 = true
                        changeMediaVolume(rangeState.toInt())
                    } else if (action is BooleanAction) {
                        toggleState2 = getMediaVolume() == 0f
                        rangeState = 50f
                        changeMediaVolume(if (toggleState2) rangeState.toInt() else 0)
                    }
                    Controls.MEDIA_VOLUME.also {
                        flow.onNext(
                            buildRangeControl(
                                id = it.id,
                                title = it.title,
                                icon = getCurrentMediaVolumeIcon(),
                                state = if (toggleState2) rangeState else 0f,
                                intent = it.intent,
                                format = getMediaVolumeFormat()
                            )
                        )
                    }
                    toggleState2 = false

                }

                Controls.RING_VOLUME.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)

                    if (action is FloatAction) {
                        rangeState = action.newValue
                        toggleState2 = true
                        changeRingVolume(rangeState.toInt())
                    } else if (action is BooleanAction) {
                        toggleState2 = getRingVolume() == 0f
                        rangeState = 50f
                        changeRingVolume(if (toggleState2) rangeState.toInt() else 0)
                    }
                    Controls.RING_VOLUME.also {
                        flow.onNext(
                            buildRangeControl(
                                id = it.id,
                                title = it.title,
                                icon = getCurrentRingVolumeIcon(),
                                state = if (toggleState2) rangeState else 0f,
                                intent = it.intent,
                                format = getRingVolumeFormat()
                            )
                        )
                    }
                }

                Controls.BRIGHTNESS.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is FloatAction) {
                        rangeState = action.newValue
                        toggleState2 = true
                        changeBrightness(applicationContext, (rangeState).toInt())
                        toggleState2 = true
                    } else if (action is BooleanAction) {
                        rangeState = 50f
                        toggleState2 = isAutoBrightnessEnabled() == true
                        enableAutoBrightness(toggleState2)
                    }
                    Controls.BRIGHTNESS.also {
                        flow.onNext(
                            buildRangeControl(
                                id = it.id,
                                title = it.title,
                                icon = getCurrentBrightnessIcon(),
                                state = rangeState,
                                intent = it.intent,
                                format = getCurrentBrightnessFormat()
                            )
                        )
                    }
                }

                Controls.AUTO_ROTATE.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState2 = action.newState
                    enableAutoRotate(toggleState2)
                    Controls.AUTO_ROTATE.also {
                        flow.onNext(
                            buildToggleControl(
                                id = it.id,
                                title = it.title,
                                enabled = toggleState2,
                                icon = getCurrentAutoRotationIcon(),
                                intent = it.intent
                            )
                        )
                    }
                }
                Controls.BATTERY_INFO.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    Controls.BATTERY_INFO.also {
                        flow.onNext(
                            buildRangeControl(
                                id = it.id,
                                title = it.title,
                                state = getBatteryPercentage().toFloat(),
                                icon = getCurrentBatteryIcon(),
                                intent = it.intent,
                                format = getBatteryFormat()
                            )
                        )
                    }

                }
                Controls.TIME.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    Controls.TIME.also {
                        flow.onNext(
                            buildRangeControl(
                                id = it.id,
                                title = getDate(),
                                time = getTime(),
                                icon = it.icon,
                                intent = it.intent
                            )
                        )
                    }
                }

                Controls.MICROPHONE.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) {
                        toggleState2 = action.newState
                        enableMicrophone(toggleState2)
                    }
                    Controls.MICROPHONE.also {
                        flow.onNext(
                            buildToggleControl(
                                id = it.id,
                                title = it.title,
                                icon = getCurrentMicrophoneIcon(),
                                intent = it.intent,
                                enabled = toggleState2
                            )
                        )
                    }
                }

                Controls.PLAYER.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) {
                        toggleState2 = !isMusicPlaying()
                        enableMusic(toggleState2)
                        currentPlayerState = if (toggleState2) "Playing" else "Paused"
                    } else if (action is FloatAction) {
                        rangeState = action.newValue
                        toggleState2 = true
                        if (rangeState < 25f) {
                            previousSong(); currentPlayerState = "Previous"
                        } else if (rangeState > 75f) {
                            nextSong(); currentPlayerState = "Next"
                        }
                    }

                    Controls.PLAYER.also {
                        flow.onNext(
                            buildRangeControl(
                                id = it.id,
                                title = it.title,
                                icon = getCurrentPlayerIcon(currentPlayerState),
                                intent = it.intent,
                                enabled = true,
                                state = if (toggleState2) 50f else 0f,
                                format = currentPlayerState
                            )
                        )
                    }
                }

                Controls.HOTSPOT.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    Controls.HOTSPOT.also {
                        flow.onNext(
                            buildToggleControl(
                                id = it.id,
                                title = it.title,
                                subTitle = it.subTitle,
                                icon = it.icon,
                                intent = it.intent,
                                enabled = isHotSpotEnabled()
                            )
                        )
                    }
                }

                Controls.WIFI.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState2 = action.newState
                    if (isRooted) {
                        enableRootWifi(toggleState2)
                    }
                    Controls.WIFI.also {
                        flow.onNext(
                            buildToggleControl(
                                id = it.id,
                                title = it.title,
                                subTitle = if (isRooted) "" else it.subTitle,
                                icon = getCurrentWifiIcon(),
                                intent = it.intent,
                                enabled = if (isRooted) toggleState2 else isWifiEnabled(),
                                stateText = getWIFIFormat()
                            )
                        )
                    }
                }

                Controls.LOCATION.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    Controls.LOCATION.also {
                        flow.onNext(
                            buildToggleControl(
                                id = it.id,
                                title = it.title,
                                subTitle = it.subTitle,
                                icon = getCurrentLocationIcon(),
                                intent = it.intent,
                                enabled = isLocationEnabled()
                            )
                        )
                    }
                }

                Controls.MOBILE_DATA.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState2 = action.newState
                    if (isRooted) {
                        enableRootMobileData(toggleState2)
                    }
                    Controls.MOBILE_DATA.also {
                        flow.onNext(
                            buildToggleControl(
                                id = it.id,
                                title = it.title,
                                subTitle = if (isRooted) "" else it.subTitle,
                                icon = it.icon,
                                intent = it.intent,
                                enabled = if (isRooted) toggleState2 else isMobileDataEnabled(),
                                stateText = getMobileDataFormat()
                            )
                        )
                    }
                }

                Controls.DND.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    Controls.DND.also {
                        flow.onNext(
                            buildToggleControl(
                                id = it.id,
                                title = it.title,
                                subTitle = it.subTitle,
                                icon = getCurrentDNDIcon(),
                                intent = it.intent,
                                enabled = isDNDEnabled()
                            )
                        )
                    }
                }

                Controls.DARK_THEME.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    Controls.DARK_THEME.also {
                        flow.onNext(
                            buildToggleControl(
                                id = it.id,
                                title = it.title,
                                subTitle = it.subTitle,
                                icon = it.icon,
                                intent = it.intent,
                                enabled = isDarkThemeEnabled()
                            )
                        )
                    }
                }

                Controls.FLIGHT_MODE.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    Controls.FLIGHT_MODE.also {
                        flow.onNext(
                            buildToggleControl(
                                id = it.id,
                                title = it.title,
                                subTitle = it.subTitle,
                                icon = getCurrentAirplaneIcon(),
                                intent = it.intent,
                                enabled = isAirplaneEnabled()
                            )
                        )
                    }
                }
                else -> consumer.accept(ControlAction.RESPONSE_OK)
            }
        }
    }

    private fun buildControl(
        id: Int,
        titleRes: String,
        subTitle: String = "",
        type: Int,
        template: ControlTemplate,
        icon: Int? = null,
        appIcon: Bitmap? = null,
        intent: Intent,
        stateText: String = ""
    ): Control {

        val pi = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return Control.StatefulBuilder(id.toString(), pi)
            .setTitle(titleRes)
            .setSubtitle(subTitle)
            .setDeviceType(type)
            .setStatus(Control.STATUS_OK)
            .setControlTemplate(template)
            .setCustomIcon(
                if (icon != null) {
                    Icon.createWithResource(
                        this,
                        icon
                    )
                } else Icon.createWithBitmap(appIcon)
            )
            .setCustomColor(ColorStateList.valueOf(backgroundIntControl()))
            .setStatusText(if (type == DeviceTypes.TYPE_THERMOSTAT) "" else stateText)
            .build()
    }

    private fun buildToggleControl(
        id: Int,
        title: String,
        enabled: Boolean = false,
        icon: Int,
        subTitle: String = "",
        intent: Intent = Intent(),
        stateText: String = ""
    ) = buildControl(
        id = id,
        template = ToggleTemplate(
            id.toString(),
            ControlButton(
                enabled,
                enabled.toString().uppercase(Locale.getDefault())
            )
        ),
        titleRes = title,
        subTitle = subTitle,
        type = DeviceTypes.TYPE_GENERIC_ON_OFF,
        icon = icon,
        intent = intent,
        stateText = stateText
    )

    private fun buildRangeControl(
        id: Int,
        title: String,
        minValue: Float = 0f,
        maxValue: Float = 100f,
        state: Float = (minValue + maxValue) / 2,
        step: Float = 1f,
        icon: Int,
        subTitle: String = "",
        isWeather: Boolean = false,
        time: Pair<String, String>? = null,
        format: String = "%1.0f%%",
        intent: Intent = Intent(),
        enabled: Boolean = true
    ) = buildControl(
        id = id,
        template = ToggleRangeTemplate(
            id.toString(),
            ControlButton(enabled, id.toString()),
            RangeTemplate(
                id.toString(),
                minValue,
                if (time != null) (24 * 60).toFloat() else maxValue,
                if (time != null) (time.first.toInt() * 60 + time.second.toInt()).toFloat() else state,
                if (isWeather) 0.01f else step,
                when {
                    isWeather -> "%1.0f " + "℃"
                    time != null -> getTimeFormat()
                    else -> format
                }
            )
        ),
        titleRes = title,
        subTitle = subTitle,
        type = DeviceTypes.TYPE_THERMOSTAT,
        icon = icon,
        intent = intent
    )

    private fun buildAppControl(
        id: Int,
        title: String,
        icon: Bitmap?,
        intent: Intent
    ) = buildControl(
        id = id,
        titleRes = title,
        appIcon = icon,
        intent = intent,
        template = ToggleTemplate(
            id.toString(),
            ControlButton(
                false,
                false.toString().uppercase(Locale.getDefault())
            )
        ),
        type = DeviceTypes.TYPE_GENERIC_ON_OFF
    )
}
