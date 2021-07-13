package com.colorata.st.extensions.presets.controls

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.colorata.st.R
import com.colorata.st.extensions.presets.SText
import com.colorata.st.extensions.pxToDp
import com.colorata.st.extensions.toEasing
import com.colorata.st.ui.theme.*

/**
 *CRange is Doubled by default
 *
 Use format like this: "@%" where "@" is current value**/
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun CRange(
    modifier: Modifier = Modifier,
    title: String = "Title",
    subTitle: String = "Tap",
    format: String? = "@%",
    state: Float = 50f,
    icon: Int = R.drawable.ic_outline_search_24,
    intent: Intent = Controls.BLUETOOTH.intent,
    isTripled: Boolean = true,
    onClick: (Boolean) -> Boolean = { it },
    onRangeChange: (Float) -> Float = { it }
) {
    val background = backgroundColorControl()
    val foreground = foregroundColorControl()

    var controlWidth by remember { mutableStateOf(1f) }
    val width =
        Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    var isSelected by remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        if (isSelected) 1.2f else 1f,
        animationSpec = FloatTweenSpec(150, 0, AnticipateOvershootInterpolator().toEasing())
    )
    var booleanState by remember { mutableStateOf(true) }
    val transition = updateTransition(targetState = booleanState, label = "")
    var rangeState by remember { mutableStateOf(state) }
    val range by transition.animateFloat(label = "") {
        if (it) 0f
        else rangeState
    }
    Card(
        modifier = modifier
            .padding(30.dp)
            .width(if (isTripled) width.dp else ((2 * width) / 3).dp)
            .requiredHeight(105.dp)
            .scale(scale.value)
            .onGloballyPositioned {
                controlWidth = pxToDp(it.size.width).toFloat()
            }
            .scrollable(
                orientation = Orientation.Horizontal,
                state = rememberScrollableState { delta ->
                    if (!booleanState) {
                        rangeState += delta / 3
                        if (rangeState < 0f) rangeState = 0f
                        else if (rangeState > 100f) rangeState = 100f
                    } else {
                        booleanState = !booleanState
                        rangeState = 0f
                        if (rangeState < 0f) rangeState = 0f
                        else if (rangeState > 100f) rangeState = 100f
                        rangeState += delta / 3
                    }
                    onRangeChange(((controlWidth - 105) / 100) * range)
                    delta
                }
            )
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    isSelected = false
                }, onTap = {
                    isSelected = false
                    booleanState = !booleanState
                    booleanState = onClick(booleanState)
                }, onLongPress = {
                    context.startActivity(intent)
                    activity.overridePendingTransition(R.anim.app_in, R.anim.app_out)
                })
            },
        shape = RoundedCornerShape(50),
        backgroundColor = background
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .height(105.dp)
                    .defaultMinSize(minWidth = 105.dp)
                    .width(105.dp + (((controlWidth - 105) / 100) * range).dp)
                    .clip(RoundedCornerShape(50))
                    .background(foreground)
            ) {

            }
            Column(
                modifier = Modifier
                    .padding(start = 30.dp, top = 20.dp, bottom = 20.dp)
                    .matchParentSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                SText(
                    text = format?.replace("@", range.toInt().toString()) ?: range.toInt().toString() + "%",
                    textColor = Color(SystemColor.BLUE.primaryHex.toIntColor()),
                    fontSize = SDimens.buttonText,
                    fontWeight = FontWeight.Light
                )
                SText(
                    text = title, textColor = Color.White,
                    fontSize = SDimens.cardTitle
                )
                SText(
                    text = subTitle, textColor = Color.White.copy(alpha = 0.3f),
                    fontSize = SDimens.subTitle
                )
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    alpha = 0.1f,
                    modifier = Modifier
                        .fillMaxHeight()
                        .scale(1.5f)
                )
            }
        }
    }
}