package com.colorata.st

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
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
import android.service.controls.templates.ControlButton
import android.service.controls.templates.ControlTemplate
import android.service.controls.templates.RangeTemplate
import android.service.controls.templates.ToggleTemplate
import com.colorata.st.extensions.*
import com.colorata.st.extensions.weather.WeatherResponse
import com.colorata.st.extensions.weather.WeatherService
import com.colorata.st.ui.theme.Controls
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.backgroundIntControl
import com.colorata.st.ui.theme.getApps
import io.reactivex.Flowable
import io.reactivex.processors.ReplayProcessor
import org.reactivestreams.FlowAdapters
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.Flow
import java.util.function.Consumer


class PowerControls : ControlsProviderService() {
    private val controlFlows =
        mutableMapOf<String, ReplayProcessor<Control>>()
    private var toggleState2 = false
    private var rangeState = 50f
    private val controls: MutableList<Control>
        get() {
            val list = mutableListOf<Control>()
            val shared = getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)

            var minDegrees = shared.getInt(Strings.minDegrees, -50).toFloat()
            var maxDegrees = shared.getInt(Strings.maxDegrees, 50).toFloat()

            var currentRight = shared.getString("CurrentRightWeather", "")
            var currentFeels = shared.getString("CurrentFeelsWeather", "")
            var currentFloat = shared.getFloat("CurrentFloatWeather", 0f)

            val city = shared.getString(Strings.city, "Moscow").toString()

            list.clear()

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
                        currentFeels = "Feels: " + weatherResponse.main!!.feels + " \u2103"
                        currentFloat = weatherResponse.main!!.temp

                        val edit = shared.edit()
                        edit.putString("CurrentRightWeather", currentRight)
                        edit.putString("CurrentFeelsWeather", currentFeels)
                        edit.putFloat("CurrentFloatWeather", currentFloat)
                        edit.apply()

                        list.add(
                            buildRangeControl(
                                id = 1441,
                                title = currentRight!!,
                                icon = R.drawable.ic_outline_cloud_24,
                                state = currentFloat,
                                subTitle = currentFeels!!,
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
                shared.edit().putInt(Strings.minDegrees, minDegrees.toInt()).apply()
            } else if (currentFloat > maxDegrees) {
                maxDegrees = currentFloat
                shared.edit().putInt(Strings.maxDegrees, maxDegrees.toInt()).apply()
            }


            list.add(
                buildRangeControl(
                    id = 1441,
                    title = currentRight!!,
                    icon = R.drawable.ic_outline_cloud_24,
                    state = currentFloat,
                    subTitle = currentFeels!!,
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
                            title = control.title,
                            subTitle = control.subTitle,
                            icon = control.icon,
                            state = when (control.id) {
                                Controls.BRIGHTNESS.id -> (getBrightness() / 2.55).toFloat()
                                Controls.RING_VOLUME.id -> getRingVolume()
                                Controls.MEDIA_VOLUME.id -> getMediaVolume()
                                else -> 50f
                            },
                            intent = control.intent
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
                                        subTitle = control.subTitle,
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
                                        subTitle = control.subTitle,
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
                                        subTitle = control.subTitle,
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
                                        subTitle = control.subTitle,
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
                                    subTitle = control.subTitle,
                                    icon = control.icon,
                                    enabled = when (control.id) {
                                        Controls.WIFI.id -> isWifiEnabled()
                                        Controls.BLUETOOTH.id -> isBluetoothEnabled()
                                        Controls.MOBILE_DATA.id -> isMobileDataEnabled()
                                        Controls.LOCATION.id -> isLocationEnabled()
                                        Controls.BATTERY_SAVER.id -> isBatterySaverEnabled()
                                        Controls.AUTO_ROTATE.id -> isAutoRotationEnabled()
                                        Controls.DND.id -> isDNDEnabled()
                                        Controls.NIGHT_LIGHT.id -> isDarkThemeEnabled()
                                        Controls.FLIGHT_MODE.id -> isAirplaneEnabled()
                                        else -> false
                                    },
                                    intent = control.intent
                                )
                            )
                        }
                    }
                }
            }

            val title = shared.getString(Strings.linkTitle, "Google") ?: "Google"
            val link =
                shared.getString(Strings.linkLink, "https://google.com") ?: "https://google.com"
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
            /*val apps = getApps()
            getApps().forEach {
                buildToggleControl(
                    id = apps.indexOf(it),
                    title = it.name,
                    subTitle = Strings.tap,
                    icon = R.drawable.ic_android_black_24dp,
                    enabled = false
                )
            }
*/
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
        val shared = getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
        controlFlows[controlId]?.let { flow ->
            when (controlId) {

                0.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    val link = shared.getString(Strings.linkLink, "https://google.com")
                        ?: "https://google.com"
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
                                subTitle = it.subTitle,
                                enabled = toggleState2,
                                icon = it.icon,
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
                                subTitle = it.subTitle,
                                enabled = toggleState2,
                                icon = it.icon,
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
                        changeMediaVolume(rangeState.toInt())
                    } else if (action is BooleanAction) {
                        toggleState2 = action.newState
                        changeMediaVolume(0)
                    }
                    Controls.MEDIA_VOLUME.also {
                        flow.onNext(
                            buildRangeControl(
                                id = it.id,
                                title = it.title,
                                subTitle = it.subTitle,
                                icon = it.icon,
                                state = rangeState,
                                intent = it.intent
                            )
                        )
                    }

                }

                Controls.RING_VOLUME.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is FloatAction) {
                        rangeState = action.newValue
                        changeRingVolume(rangeState.toInt())
                    } else if (action is BooleanAction) {
                        toggleState2 = action.newState
                        changeRingVolume(0)
                    }
                    Controls.RING_VOLUME.also {
                        flow.onNext(
                            buildRangeControl(
                                id = it.id,
                                title = it.title,
                                subTitle = it.subTitle,
                                icon = it.icon,
                                state = rangeState,
                                intent = it.intent
                            )
                        )
                    }
                }

                Controls.BRIGHTNESS.id.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is FloatAction) {
                        rangeState = action.newValue
                        changeBrightness(applicationContext, (rangeState * 2.55).toInt())
                    } else if (action is BooleanAction) {
                        toggleState2 = action.newState
                        changeBrightness(applicationContext, 0)
                    }
                    Controls.BRIGHTNESS.also {
                        flow.onNext(
                            buildRangeControl(
                                id = it.id,
                                title = it.title,
                                subTitle = it.subTitle,
                                icon = it.icon,
                                state = rangeState,
                                intent = it.intent
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
                                subTitle = it.subTitle,
                                enabled = toggleState2,
                                icon = it.icon,
                                intent = it.intent
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
        intent: Intent
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
            .build()
    }

    private fun buildToggleControl(
        id: Int,
        title: String,
        enabled: Boolean = false,
        icon: Int,
        subTitle: String = "",
        intent: Intent = Intent()
    ) = buildControl(
        id = id,
        template = ToggleTemplate(
            id.toString(),
            ControlButton(
                enabled,
                enabled.toString().toUpperCase(Locale.getDefault())
            )
        ),
        titleRes = title,
        subTitle = subTitle,
        type = DeviceTypes.TYPE_GENERIC_ON_OFF,
        icon = icon,
        intent = intent
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
        intent: Intent = Intent()
    ) = buildControl(
        id = id,
        template = RangeTemplate(
            id.toString(),
            minValue,
            maxValue,
            state,
            if (isWeather) 0.01f else step,
            if (isWeather) "%1.1f " + "℃" else "%1.0f%%"
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
                false.toString().toUpperCase(Locale.getDefault())
            )
        ),
        type = DeviceTypes.TYPE_GENERIC_ON_OFF
    )
}
