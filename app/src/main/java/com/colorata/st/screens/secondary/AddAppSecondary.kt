package com.colorata.st.screens.secondary

import android.graphics.Bitmap
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.colorata.st.R
import com.colorata.st.extensions.getAppsIcon
import com.colorata.st.extensions.getAppsLabel
import com.colorata.st.extensions.getAppsPackage
import com.colorata.st.extensions.presets.SText
import com.colorata.st.extensions.presets.SToggle
import com.colorata.st.extensions.presets.Title
import com.colorata.st.ui.theme.*

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
    //val checkedApps = context.getApps()
    val labels = mutableListOf("Title")
    labels.addAll(context.getAppsLabel())

    val packages = mutableListOf("Title")
    packages.addAll(context.getAppsPackage())

    val enabled = mutableListOf<Boolean>()
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
        /*var checked = false
        for (j in checkedApps) {
            if (packages[i] == j.id) {
                enabled.add(true)
                checked = true
                break
            }
        }
        if (!checked) enabled.add(false)*/
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
                                    //context.deleteApp(packages[index], labels[index])
                                }) {
                                enabled[index] = true
                                //context.updateApp(packages[index], labels[index])
                            }
                        }
                    }
                }
            }
        }
    }
}