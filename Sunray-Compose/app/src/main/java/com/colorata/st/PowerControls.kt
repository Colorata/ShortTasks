package com.colorata.st

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
import android.util.Log
import com.colorata.st.activities.MainActivity
import com.colorata.st.extensions.*
import com.colorata.st.ui.theme.Controls
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.backIntControl
import io.reactivex.Flowable
import io.reactivex.processors.ReplayProcessor
import org.reactivestreams.FlowAdapters
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
            getCurrentWeather(this)

            val shared = getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
            val currentRight = shared.getString("CurrentRightWeather", "Error")
            val currentFeels = shared.getString("CurrentFeelsWeather", "Error")
            val currentFloat = shared.getFloat("CurrentFloatWeather", 0.0f)

            list.clear()

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


            Controls.values().forEach { control ->
                if (control.isRange){
                    list.add(buildRangeControl(
                        id = control.id,
                        title = control.title,
                        icon = control.icon,
                        state = when(control.id) {
                            1514 -> (getBrightness(applicationContext)/2.55).toFloat()
                            1513 -> getRingVolume(this)
                            1512 -> getMediaVolume(this)
                            else -> 50f
                        }
                    ))
                } else {
                    list.add(buildToggleControl(
                        id = control.id,
                        title = control.title,
                        icon = control.icon,
                        enabled = when (control.id) {
                            //1501 -> isHotspotEnabled(this)
                            1502 -> isWifiEnabled(this)
                            1504 -> isBluetoothEnabled()
                            1505 -> isMobileDataEnabled(this)
                            1507 -> isLocationEnabled(this)
                            1509 -> isBatterySaverEnabled(this)
                            1515 -> isAutoRotationEnabled(this)
                            1516 -> isDNDEnabled(this)
                            1517 -> isNightLightEnabled()
                            1518 -> isFlightModeEnabled(this)
                            else -> false
                        }
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

                1512.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is FloatAction) rangeState = action.newValue
                    changeMediaVolume(this, rangeState.toInt())
                    flow.onNext(buildRangeControl(
                        id = 1512,
                        title = Controls.MEDIA_VOLUME.title,
                        icon = Controls.MEDIA_VOLUME.icon,
                        state = rangeState
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
                        state = rangeState
                    ))
                }

                1514.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is FloatAction) rangeState = action.newValue
                    changeBrightness(applicationContext, (rangeState*2.55).toInt())
                    Log.d("Brightness", (getBrightness(applicationContext)/2.55).toString())
                    flow.onNext(buildRangeControl(
                        id = 1514,
                        title = Controls.BRIGHTNESS.title,
                        icon = Controls.BRIGHTNESS.icon,
                        state = rangeState
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
                        icon = Controls.AUTO_ROTATE.icon
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
                        icon = Controls.DND.icon
                    ))
                }

                1517.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState2 = action.newState
                    enableNightLight(toggleState2)
                    flow.onNext(buildToggleControl(
                        id = 1517,
                        title = Controls.NIGHT_LIGHT.title,
                        enabled = toggleState2,
                        icon = Controls.NIGHT_LIGHT.icon
                    ))
                }

                1518.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState2 = action.newState
                    enableFlightMode(this, toggleState2)
                    flow.onNext(buildToggleControl(
                        id = 1518,
                        title = Controls.FLIGHT_MODE.title,
                        enabled = toggleState2,
                        icon = Controls.FLIGHT_MODE.icon
                    ))
                }
                else -> consumer.accept(ControlAction.RESPONSE_OK)
            }
        } ?: consumer.accept(ControlAction.RESPONSE_FAIL)
    }

    private fun buildControl(
        id: Int,
        titleRes: String,
        subTitle: String = "",
        type: Int,
        template: ControlTemplate,
        icon: Int
    ): Control {
        val pi = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
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
        subTitle: String = ""
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
        subTitle = subTitle
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
        isWeather: Boolean = false
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
        icon = icon
    )


}
