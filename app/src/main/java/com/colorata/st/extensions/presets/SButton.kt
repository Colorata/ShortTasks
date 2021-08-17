package com.colorata.st.extensions.presets

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.foregroundColor
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SButton(modifier: Modifier = Modifier, text: String = "", onClick: () -> Unit) {
    var isSelected by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val shape = remember { Animatable(50f)}
    if (!isSelected) {
        if (!shape.isRunning && shape.value != 50f) {
            scope.launch {
                shape.animateTo(50f, FloatTweenSpec(150, 0))
            }
        }
    }
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    isSelected = true
                    shape.animateTo(20f, FloatTweenSpec(150, 0))
                    tryAwaitRelease()
                    isSelected = false
                }, onTap = {
                    isSelected = false
                    onClick()
                })
            }
            .clip(RoundedCornerShape(shape.value.toInt()))
            .border(
                BorderStroke(
                    width = SDimens.borderWidth,
                    color = foregroundColor()
                ), RoundedCornerShape(shape.value.toInt())
            )
            .background(Color.Transparent)
    ) {
        SText(
            modifier = Modifier.padding(vertical = SDimens.smallPadding, horizontal = SDimens.smallPadding + 5.dp),
            text = text.uppercase(Locale.ROOT),
            fontSize = SDimens.buttonText,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Preview(name = "SButton", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun TButtonDefault() {
    SButton(text = "OutLined Button", modifier = Modifier) {

    }
}

