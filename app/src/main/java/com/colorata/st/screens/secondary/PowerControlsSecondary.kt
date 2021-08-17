package com.colorata.st.screens.secondary

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.colorata.st.extensions.presets.SText
import com.colorata.st.extensions.presets.controls.CToggle
import com.colorata.st.extensions.presets.controls.StatusCard
import com.colorata.st.extensions.pxToDp
import com.colorata.st.ui.theme.*
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun PowerControlsSecondary() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(SystemColor.BLACK.secondaryHex.toIntColor()))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val controls = Controls.values().toMutableList()
        val categories = mutableListOf("Device", "Notifications", "Media", "Widgets")
        val displayWidthPixels by remember { mutableStateOf(context.resources.displayMetrics.widthPixels) }
        StatusCard()
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .height(SDimens.borderWidth)
                .background(
                    backgroundColor().copy(alpha = 0.1f)
                )
                .padding(SDimens.smallPadding)
        )
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val scope = rememberCoroutineScope()
            if (!scrollState.isScrollInProgress) {
                if (
                    scrollState.value != 0
                    && scrollState.value != displayWidthPixels
                    && scrollState.value != displayWidthPixels * 2
                    && scrollState.value != displayWidthPixels * 3
                ) {
                    when (scrollState.value) {
                        in 0..(displayWidthPixels / 2) -> {
                            scope.launch {
                                scrollState.animateScrollTo(0)
                            }
                        }
                        in (displayWidthPixels / 2)..(displayWidthPixels * 3 / 2) -> {
                            scope.launch {
                                scrollState.animateScrollTo(displayWidthPixels)
                            }
                        }
                        in (displayWidthPixels * 3 / 2)..(displayWidthPixels * 5 / 2) -> {
                            scope.launch {
                                scrollState.animateScrollTo(displayWidthPixels * 2)
                            }
                        }
                        in (displayWidthPixels * 5 / 2)..(displayWidthPixels * 7 / 2) -> {
                            scope.launch {
                                scrollState.animateScrollTo(displayWidthPixels * 3)
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(vertical = SDimens.largePadding)
            ) {
                categories.forEach {
                    SText(text = it, modifier = Modifier.width(pxToDp(displayWidthPixels).dp))
                }
            }
        }
        Row(modifier = Modifier.horizontalScroll(scrollState)) {
            categories.forEachIndexed { i, item ->
                Column {
                    controls.forEachIndexed { index, control ->
                        CToggle(
                            title = control.title,
                            subTitle = control.subTitle,
                            icon = control.icon,
                            isTripled = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}