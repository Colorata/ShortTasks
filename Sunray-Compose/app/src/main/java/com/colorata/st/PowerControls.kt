package com.colorata.st

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Icon
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
import com.colorata.st.ui.theme.backIntControl
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

            var currentRight = shared.getString("CurrentRightWeather" , "")
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

                        list.add(buildRangeControl(
                            id = 1441,
                            title = currentRight!!,
                            icon = R.drawable.ic_outline_cloud_24,
                            state = currentFloat,
                            subTitle = currentFeels!!,
                            isWeather = true,
                            minValue = -50f,
                            maxValue = 50f
                        ))
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
            } else if (currentFloat > maxDegrees){
                maxDegrees = currentFloat
                shared.edit().putInt(Strings.maxDegrees, maxDegrees.toInt()).apply()
            }


            list.add(buildRangeControl(
                id = 1441,
                title = currentRight!!,
                icon = R.drawable.ic_outline_cloud_24,
                state = currentFloat,
                subTitle = currentFeels!!,
                isWeather = true,
                minValue = minDegrees,
                maxValue = maxDegrees
            ))

            Controls.values().forEach { control ->
                if (control.isRange){
                    list.add(buildRangeControl(
                        id = control.id,
                        title = control.title,
                        icon = control.icon,
                        state = when(control.id) {
                            1514 -> (this.getBrightness()/2.55).toFloat()
                            1513 -> this.getRingVolume()
                            1512 -> this.getMediaVolume()
                            else -> 50f
                        },
                        intent = control.intent
                    ))
                } else {
                    list.add(buildToggleControl(
                        id = control.id,
                        title = control.title,
                        icon = control.icon,
                        enabled = when (control.id) {
                            1502 -> this.isWifiEnabled()
                            1504 -> isBluetoothEnabled()
                            1505 -> this.isMobileDataEnabled()
                            1507 -> this.isLocationEnabled()
                            1509 -> this.isBatterySaverEnabled()
                            1515 -> this.isAutoRotationEnabled()
                            1516 -> this.isDNDEnabled()
                            1517 -> this.isDarkThemeEnabled()
                            1518 -> this.isAirplaneEnabled()
                            else -> false
                        },
                        intent = control.intent
                    ))
                }
            }
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

        for (i in controls){
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

                1441.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    flow.onNext(controls[0])
                }

                1503.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState2 = action.newState
                    this.enableFlashlight(toggleState2)
                    flow.onNext(buildToggleControl(
                        id = 1503,
                        title = Controls.FLASHLIGHT.title,
                        enabled = toggleState2,
                        icon = Controls.FLASHLIGHT.icon,
                        intent = Controls.FLASHLIGHT.intent
                    ))
                }
                1504.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState2 = action.newState
                    enableBluetooth(toggleState2)
                    flow.onNext(buildToggleControl(
                        id = 1504,
                        title = Controls.BLUETOOTH.title,
                        enabled = toggleState2,
                        icon = Controls.BLUETOOTH.icon,
                        intent = Controls.BLUETOOTH.intent
                    ))
                }

                1512.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is FloatAction) rangeState = action.newValue
                    changeMediaVolume(this, rangeState.toInt())
                    flow.onNext(buildRangeControl(
                        id = 1512,
                        title = Controls.MEDIA_VOLUME.title,
                        icon = Controls.MEDIA_VOLUME.icon,
                        state = rangeState,
                        intent = Controls.MEDIA_VOLUME.intent
                    ))
                }

                1513.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is FloatAction) rangeState = action.newValue
                    changeRingVolume(this, rangeState.toInt())
                    flow.onNext(buildRangeControl(
                        id = 1513,
                        title = Controls.RING_VOLUME.title,
                        icon = Controls.RING_VOLUME.icon,
                        state = rangeState,
                        intent = Controls.RING_VOLUME.intent
                    ))
                }

                1514.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is FloatAction) rangeState = action.newValue
                    changeBrightness(applicationContext, (rangeState*2.55).toInt())
                    flow.onNext(buildRangeControl(
                        id = 1514,
                        title = Controls.BRIGHTNESS.title,
                        icon = Controls.BRIGHTNESS.icon,
                        state = rangeState,
                        intent = Controls.BRIGHTNESS.intent
                    ))
                }

                1515.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState2 = action.newState
                    enableAutoRotate(this, toggleState2)
                    flow.onNext(buildToggleControl(
                        id = 1515,
                        title = Controls.AUTO_ROTATE.title,
                        enabled = toggleState2,
                        icon = Controls.AUTO_ROTATE.icon,
                        intent = Controls.AUTO_ROTATE.intent
                    ))
                }

                1516.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState2 = action.newState
                    enableDND(this, toggleState2)
                    flow.onNext(buildToggleControl(
                        id = 1516,
                        title = Controls.DND.title,
                        enabled = toggleState2,
                        icon = Controls.DND.icon,
                        intent = Controls.DND.intent
                    ))
                }

                1517.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState2 = action.newState
                    enableDarkMode(this, toggleState2)
                    flow.onNext(buildToggleControl(
                        id = 1517,
                        title = Controls.NIGHT_LIGHT.title,
                        enabled = toggleState2,
                        icon = Controls.NIGHT_LIGHT.icon,
                        intent = Controls.NIGHT_LIGHT.intent
                    ))
                }

                1518.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState2 = action.newState
                    flow.onNext(buildToggleControl(
                        id = 1518,
                        title = Controls.FLIGHT_MODE.title,
                        enabled = toggleState2,
                        icon = Controls.FLIGHT_MODE.icon
                    ))
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
        icon: Int,
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
                Icon.createWithResource(
                    this,
                    icon
                )
            )
            .setCustomColor(ColorStateList.valueOf(backIntControl(this)))
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
        type = DeviceTypes.TYPE_GENERIC_ON_OFF,
        icon = icon,
        subTitle = subTitle,
        intent = intent
    )

    private fun buildRangeControl(
        id: Int,
        title: String,
        minValue: Float = 0f,
        maxValue: Float = 100f,
        state: Float = (minValue + maxValue)/2,
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
            if(isWeather) 0.01f else step,
            if(isWeather) "%1.2f " + "℃" else "%1.0f%%"
        ),
        titleRes = title,
        subTitle = subTitle,
        type = DeviceTypes.TYPE_THERMOSTAT,
        icon = icon,
        intent = intent
    )

}
