package com.colorata.st

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TaskStackBuilder
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
import com.colorata.st.ui.theme.*
import io.reactivex.Flowable
import io.reactivex.processors.ReplayProcessor
import org.reactivestreams.FlowAdapters
import java.util.*
import java.util.concurrent.Flow
import java.util.function.Consumer

private const val TOGGLE_ID = 1337
private const val RANGE_ID = 1338

class PowerControls : ControlsProviderService() {
    private val controlFlows =
        mutableMapOf<String, ReplayProcessor<Control>>()
    private var toggleState1 = false
    private var toggleState2 = false
    private var rangeState = 50f

    private var controls: MutableList<Control> = mutableListOf()

    override fun createPublisherForAllAvailable(): Flow.Publisher<Control> =
        FlowAdapters.toFlowPublisher(
            Flowable.fromIterable(
                controls
            )
        )

    override fun createPublisherFor(controlIds: List<String>): Flow.Publisher<Control> {
        val flow: ReplayProcessor<Control> = ReplayProcessor.create(controlIds.size)

        controlIds.forEach { controlFlows[it] = flow }

        getCurrentWeather(this)

        val shared = getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
        val currentRight = shared.getString("CurrentRightWeather", "Error")
        val currentFeels = shared.getString("CurrentFeelsWeather", "Error")
        val currentFloat = shared.getFloat("CurrentFloatWeather", 0.0f)

        controls.clear()

        controls.add(buildRangeControl(
            id = 1441,
            title = currentRight!!,
            icon = R.drawable.ic_outline_cloud_24,
            state = currentFloat,
            subTitle = currentFeels!!,
            isWeather = true
        ))

        Controls.values().forEach { control ->
            if (control.isRange){
                controls.add(buildRangeControl(
                    id = control.id,
                    title = control.title,
                    icon = control.icon,
                ))
            } else {
                controls.add(buildToggleControl(
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
                        else -> false
                    }
                ))
            }
        }

        Log.d("Controls", controls.size.toString())
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
                TOGGLE_ID.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState1 = action.newState
                    controls[0] = buildToggleControl(
                        id = TOGGLE_ID,
                        title = if (toggleState1) "SubTitle" else "Title",
                        enabled = toggleState1,
                        icon = R.drawable.ic_outline_bubble_chart_24
                    )
                    flow.onNext(controls[0])
                }
                RANGE_ID.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is FloatAction) rangeState = action.newValue
                    flow.onNext(buildRangeControl(
                        id = 1338,
                        title = "Range",
                        icon = R.drawable.ic_outline_check_circle_outline_24,
                        state = rangeState
                    ))
                }
                1440.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    if (action is BooleanAction) toggleState2 = action.newState
                    flow.onNext(buildToggleControl(
                        id = 1440,
                        title = if (toggleState2) "First" else "Second",
                        enabled = toggleState2,
                        icon = R.drawable.ic_outline_bubble_chart_24
                    ))
                }
                1441.toString() -> {
                    consumer.accept(ControlAction.RESPONSE_OK)
                    flow.onNext(controls[0])
                }
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
            Intent(),
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
        minValue: Float = -50f,
        maxValue: Float = 50f,
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
            if(isWeather) "%1.2f " + "℃" else "%1.0f"
        ),
        titleRes = title,
        subTitle = subTitle,
        type = DeviceTypes.TYPE_THERMOSTAT,
        icon = icon
    )

}