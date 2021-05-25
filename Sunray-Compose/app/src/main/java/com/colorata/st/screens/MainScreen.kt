package com.colorata.st.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.colorata.st.CurrentScreen
import com.colorata.st.activities.SecondaryActivity
import com.colorata.st.extensions.getBottomNavigationHeight
import com.colorata.st.extensions.presets.SButton
import com.colorata.st.extensions.presets.SText
import com.colorata.st.extensions.presets.Screen
import com.colorata.st.extensions.presets.TButtonDefault
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.ScreenComponents
import com.colorata.st.ui.theme.Strings


@ExperimentalAnimationApi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainScreen() {
    Screen(
        titles = ScreenComponents.MainScreen.titles,
        subTitles = ScreenComponents.MainScreen.subTitles,
        icons = ScreenComponents.MainScreen.icons,
        modifier = Modifier.padding(bottom = getBottomNavigationHeight()),
        hidden = listOf(
            { TButtonDefault() },
            { GetStartedCardContent() },
            { PowerMainScreenContent() })
    )
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun PowerMainScreenContent() {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .padding(SDimens.largePadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SButton(modifier = Modifier, text = Strings.show) {
            val intent = Intent(context, SecondaryActivity::class.java)
            intent.putExtra(Strings.screen, CurrentScreen.POWER)
            context.startActivity(intent)
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun GetStartedCardContent() {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    var visibleLocation by remember { mutableStateOf(false) }
    var visibleAccessibility by remember { mutableStateOf(false) }
    var visibleModifySettings by remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(SDimens.largePadding)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(
                modifier = Modifier.padding(
                    end = SDimens.smallPadding,
                    bottom = SDimens.smallPadding
                ), text = Strings.modifySettings
            ) {
                visibleModifySettings = !visibleModifySettings
                visibleAccessibility = false
                visibleLocation = false
            }

            SButton(modifier = Modifier, text = Strings.other) {
                visibleLocation = !visibleLocation
                visibleModifySettings = false
                visibleAccessibility = false
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(
                modifier = Modifier.padding(bottom = SDimens.smallPadding),
                text = Strings.accessibility
            ) {
                visibleAccessibility = !visibleAccessibility
                visibleLocation = false
                visibleModifySettings = false
            }
        }

        AnimatedVisibility(visible = visibleLocation) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SText(
                    text = if (context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_DENIED
                    ) Strings.whyLocation else Strings.alreadyGranted,
                    fontSize = SDimens.subTitle,
                    modifier = Modifier
                        .padding(end = SDimens.smallPadding)
                        .weight(2f)
                )
                SButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), text = Strings.ok
                ) {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        0
                    )
                    if (context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
                    )
                        visibleLocation = false
                }
            }
        }

        AnimatedVisibility(visible = visibleAccessibility) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SText(
                    text = if (context.checkCallingOrSelfPermission(Manifest.permission.BIND_ACCESSIBILITY_SERVICE) ==
                        PackageManager.PERMISSION_DENIED
                    ) Strings.whyAccessibility else Strings.alreadyGranted,
                    fontSize = SDimens.subTitle,
                    modifier = Modifier
                        .padding(end = SDimens.smallPadding)
                        .weight(2f)
                )
                SButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), text = Strings.ok
                ) {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)

                    if (context.checkCallingOrSelfPermission(Manifest.permission.BIND_ACCESSIBILITY_SERVICE) ==
                        PackageManager.PERMISSION_GRANTED
                    )
                        visibleAccessibility = false
                }
            }
        }

        AnimatedVisibility(visible = visibleModifySettings) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SText(
                    text = if (Settings.System.canWrite(context)) Strings.whyModifySettings
                    else Strings.alreadyGranted,
                    fontSize = SDimens.subTitle,
                    modifier = Modifier
                        .padding(end = SDimens.smallPadding)
                        .weight(2f)

                )
                SButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), text = Strings.ok
                ) {
                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)

                    if (Settings.System.canWrite(context))
                        visibleModifySettings = false
                }
            }
        }
    }
}