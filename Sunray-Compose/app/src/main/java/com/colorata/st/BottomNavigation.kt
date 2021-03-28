package com.colorata.st


import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.colorata.st.extensions.pxToDp
import com.colorata.st.screens.FeatureScreen
import com.colorata.st.screens.MainScreen
import com.colorata.st.screens.MoreScreen
import com.colorata.st.ui.theme.backColor
import com.colorata.st.ui.theme.buttonColor

@Composable
fun BottomNav(state: MutableState<CurrentScreen>){

    val shared = LocalContext.current.getSharedPreferences("Shared", Context.MODE_PRIVATE)
    BottomNavigation(
        contentColor = buttonColor(LocalContext.current),
        backgroundColor = backColor(LocalContext.current),
        modifier = Modifier.onGloballyPositioned {
            shared.edit().putInt("bottomSize", pxToDp(it.size.component2())).apply()
        }
    ) {

        BottomNavigationItem(
            selected = state.value == CurrentScreen.MAIN,
            onClick = { state.value = CurrentScreen.MAIN},
            icon = {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowUp,
                    contentDescription = ""
                )
            },
            label = { Text(text = "Main")},
            alwaysShowLabel = false,
        )

        BottomNavigationItem(
            selected = state.value == CurrentScreen.FEATURES,
            onClick = { state.value = CurrentScreen.FEATURES},
            icon = {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = ""
                )
            },
            label = { Text(text = "Features")},
            alwaysShowLabel = false,

        )

        BottomNavigationItem(
            selected = state.value == CurrentScreen.MORE,
            onClick = { state.value = CurrentScreen.MORE },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = ""
                )
            },
            label = { Text(text = "More")},
            alwaysShowLabel = false
        )
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Current(state: CurrentScreen, modifier: Modifier){
    Column(modifier = modifier) {
        Crossfade(targetState = state) { screen ->
            Surface {
                when (screen){
                    CurrentScreen.MAIN -> MainScreen()
                    CurrentScreen.FEATURES -> FeatureScreen()
                    CurrentScreen.MORE -> MoreScreen()
                    CurrentScreen.BUBBLE -> TODO()
                    CurrentScreen.POWER -> TODO()
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Preview
@Composable
fun MainUI(){

    val state = rememberSaveable { mutableStateOf(CurrentScreen.MAIN)}
    Column {
        Current(state = state.value, modifier = Modifier.weight(1f))
        BottomNav(state = state)
    }
}