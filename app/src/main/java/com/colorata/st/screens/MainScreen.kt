package com.colorata.st.screens

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.colorata.st.CurrentScreen
import com.colorata.st.extensions.getBottomNavigationHeight
import com.colorata.st.extensions.goToSecondary
import com.colorata.st.extensions.presets.SButton
import com.colorata.st.extensions.presets.SText
import com.colorata.st.extensions.presets.Screen
import com.colorata.st.extensions.presets.TButtonDefault
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.ScreenComponents
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.SuperStore


@ExperimentalAnimationApi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainScreen() {
    val isNewUser = SuperStore(LocalContext.current).catchBoolean(Strings.isFirst)
    Screen(
        titles = if (isNewUser) ScreenComponents.MainScreenGetStarted.titles else ScreenComponents.MainScreen.titles,
        subTitles = if (isNewUser) ScreenComponents.MainScreenGetStarted.subTitles else ScreenComponents.MainScreen.subTitles,
        icons = if (isNewUser) ScreenComponents.MainScreenGetStarted.icons else ScreenComponents.MainScreen.icons,
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
        SButton(text = Strings.show) {
            context.goToSecondary(CurrentScreen.POWER)
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun GetStartedCardContent() {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    var visibleDND by remember { mutableStateOf(false) }
    var visiblePhone by remember { mutableStateOf(false) }
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
                visibleDND = false
                visibleAccessibility = false
                visiblePhone = false
            }

            SButton(text = Strings.other) {
                visiblePhone = !visiblePhone
                visibleDND = false
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
                modifier = Modifier.padding(
                    end = SDimens.smallPadding,
                    bottom = SDimens.smallPadding
                ),
                text = Strings.accessibility
            ) {
                visibleAccessibility = !visibleAccessibility
                visibleDND = false
                visiblePhone = false
                visibleModifySettings = false
            }
            SButton(
                modifier = Modifier.padding(bottom = SDimens.smallPadding),
                text = Strings.dnd
            ) {
                visibleDND = !visibleDND
                visibleAccessibility = false
                visiblePhone = false
                visibleModifySettings = false
            }
        }

        AnimatedVisibility(visible = visiblePhone) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SText(
                    text = if (context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) ==
                        PackageManager.PERMISSION_DENIED
                    ) Strings.whyPhone else Strings.alreadyGranted,
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
                        arrayOf(Manifest.permission.READ_PHONE_STATE),
                        0
                    )
                    if (context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) ==
                        PackageManager.PERMISSION_GRANTED
                    )
                        visiblePhone = false
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
                    text = if (!Settings.System.canWrite(context)) Strings.whyModifySettings
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

        AnimatedVisibility(visible = visibleDND) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SText(
                    text = if (!(context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).isNotificationPolicyAccessGranted) Strings.whyDND
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
                    val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    context.startActivity(intent)

                    if ((context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).isNotificationPolicyAccessGranted)
                        visibleDND = false
                }
            }
        }
    }
}