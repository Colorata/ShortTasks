package com.colorata.st.extensions.presets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colorata.st.R
import com.colorata.st.extensions.pxToDp
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.backgroundColor
import com.colorata.st.ui.theme.foregroundColor

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun SToggle(
    enabled: Boolean,
    modifier: Modifier = Modifier,
    enabledText: String = "On",
    disabledText: String = "Off",
    onDisable: () -> Unit,
    onEnable: () -> Unit
) {
    var state by remember { mutableStateOf(enabled) }
    var height by remember { mutableStateOf(0) }
    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = SDimens.borderWidth,
            color = foregroundColor()
        ),
        backgroundColor = backgroundColor(),
        modifier = modifier,
        onClick = {
            state = !state
            if (state) onEnable()
            else onDisable()
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .onGloballyPositioned {
                        height = pxToDp(it.size.height)
                    }, verticalAlignment = Alignment.CenterVertically
            ) {
                SText(
                    text = disabledText,
                    modifier = Modifier
                        .padding(SDimens.smallPadding),
                    fontSize = SDimens.buttonText
                )
                AnimatedVisibility(visible = !state) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_outline_check_24),
                        contentDescription = "",
                        modifier = Modifier.padding(end = SDimens.smallPadding),
                        colorFilter = ColorFilter.tint(foregroundColor())
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .width(SDimens.borderWidth)
                    .background(
                        foregroundColor()
                    )
                    .height(height.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SText(
                    text = enabledText,
                    modifier = Modifier
                        .padding(SDimens.smallPadding),
                    fontSize = SDimens.buttonText
                )
                AnimatedVisibility(visible = state) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_outline_check_24),
                        contentDescription = "",
                        modifier = Modifier.padding(end = SDimens.smallPadding),
                        colorFilter = ColorFilter.tint(foregroundColor())
                    )
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview
@Composable
private fun SToggleDefault() {
    MaterialTheme {
        SToggle(enabled = false, onEnable = {}, onDisable = {})
    }
}