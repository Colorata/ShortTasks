package com.colorata.st.screens.secondary

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.colorata.st.R
import com.colorata.st.extensions.*
import com.colorata.st.extensions.presets.SButton
import com.colorata.st.extensions.presets.SText
import com.colorata.st.extensions.presets.SToggle
import com.colorata.st.extensions.presets.Title
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.backgroundColor
import com.colorata.st.ui.theme.foregroundColor

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Preview(showSystemUi = true)
@Composable
fun AddAppSecondary() {
    val state by remember { mutableStateOf(ScrollableState { 0f }) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor())
            .scrollable(state = state, orientation = Orientation.Vertical)
    ) {
        AppsList()
    }

}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun AppsList() {
    val context = LocalContext.current
    val shared = context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    
    val labels = mutableListOf("Title")
    labels.addAll(context.getAppsLabel())

    val packages = mutableListOf("Title")
    packages.addAll(context.getAppsPackage())

    val enabled = mutableListOf<Boolean>()
    val checkedAppPackages = setOf<String>()
    val checkedAppNames = setOf<String>()
    val icons = mutableListOf(
        ContextCompat.getDrawable(
            context,
            R.drawable.ic_outline_flight_24
        )
    )

    icons.addAll(context.getAppsIcon())

    val bitmap = mutableListOf<Bitmap>()

    for (i in 0..icons.lastIndex) {
        bitmap.add(icons[i]!!.toBitmap())
        enabled.add(false)
    }

    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        itemsIndexed(items = labels) { index, _ ->
            if (index == 0) {
                Title(
                    title = Strings.addApp,
                    subTitle = Strings.addAppSubTitle
                )
            } else {
                Card(
                    shape = RoundedCornerShape(SDimens.roundedCorner),
                    border = BorderStroke(
                        width = SDimens.borderWidth,
                        color = foregroundColor()
                    ),
                    backgroundColor = backgroundColor(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SDimens.cardPadding)
                ) {

                    Column {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(SDimens.normalPadding)
                                .fillMaxWidth()
                        ) {
                            Image(
                                bitmap = bitmap[index].asImageBitmap(),
                                contentDescription = "",
                                modifier = Modifier.size(50.dp)
                            )
                            SText(
                                text = labels[index],
                                fontSize = SDimens.subTitle,
                                modifier = Modifier.padding(horizontal = SDimens.largePadding)
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(
                                    end = SDimens.normalPadding,
                                    bottom = SDimens.normalPadding,
                                    top = SDimens.smallPadding
                                )
                                .fillMaxWidth()
                        ) {
                            SToggle(
                                enabled = enabled[index],
                                onDisable = {
                                    enabled[index] = false
                                    checkedAppPackages.minus(packages[index])
                                    checkedAppNames.minus(labels[index])
                                }) {
                                enabled[index] = true

                            }
                        }
                    }
                }
            }
        }
    }
}