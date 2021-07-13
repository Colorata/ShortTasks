package com.colorata.st.extensions.presets.controls

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.colorata.st.R
import com.colorata.st.extensions.presets.SText
import com.colorata.st.extensions.toEasing
import com.colorata.st.ui.theme.*

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun CToggle(
    modifier: Modifier = Modifier,
    title: String = "Title",
    subTitle: String = "",
    stateText: String? = null,
    enabled: Boolean = false,
    isAction: Boolean = true,
    icon: Int = R.drawable.ic_outline_search_24,
    intent: Intent = Controls.BLUETOOTH.intent,
    isDoubled: Boolean = false,
    isTripled: Boolean = false,
    onClick: (Boolean) -> Boolean = { it }
) {
    val background = backgroundColorControl()
    val foreground = foregroundColorControl()

    val width = Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    var isSelected by remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        if (isSelected) 1.2f else 1f,
        animationSpec = FloatTweenSpec(150, 0, AnticipateOvershootInterpolator().toEasing())
    )
    var state by remember { mutableStateOf(enabled) }
    val color =
        animateColorAsState(
            if (state) foreground else background,
            animationSpec = tween(300)
        )
    Card(
        modifier = modifier
            .padding(30.dp)
            .width(if (isTripled) width.dp else if (isDoubled) ((2 * width) / 3).dp else  (width / 3).dp)
            .requiredHeight(105.dp)
            .scale(scale.value)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    isSelected = true
                    tryAwaitRelease()
                    isSelected = false
                }, onTap = {
                    isSelected = false
                    state = !state
                    onClick(state)
                }, onLongPress = {
                    context.startActivity(intent)
                    activity.overridePendingTransition(R.anim.app_in, R.anim.app_out)
                })
            },
        shape = RoundedCornerShape(20),
        backgroundColor = background
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color.value),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .matchParentSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                SText(
                    text = if (isAction) "" else if (state && stateText == null) Strings.on
                    else if (!state && stateText == null) Strings.off
                    else stateText
                        ?: "On",
                    textColor = if (enabled) Color(SystemColor.BLUE.primaryHex.toIntColor()) else Color.White.copy(alpha = 0.3f),
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