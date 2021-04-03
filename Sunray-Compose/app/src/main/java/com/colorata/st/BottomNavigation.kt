package com.colorata.st


import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.tween

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.colorata.st.extensions.getNavBarHeight
import com.colorata.st.extensions.pxToDp
import com.colorata.st.screens.FeatureScreen
import com.colorata.st.screens.MainScreen
import com.colorata.st.screens.MoreScreen
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.backColor
import com.colorata.st.ui.theme.buttonColor

/*@Composable
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
            label = { Text(text = Strings.main)},
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
            label = { Text(text = Strings.features)},
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
            label = { Text(text = Strings.more)},
            alwaysShowLabel = false
        )
    }
}*/

@Composable
fun BottomNav(navController: NavController){

    val shared = LocalContext.current.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    BottomNavigation(
        contentColor = buttonColor(LocalContext.current),
        backgroundColor = backColor(LocalContext.current),
        modifier = Modifier
            .padding(bottom = getNavBarHeight())
            .onGloballyPositioned {
                shared
                    .edit()
                    .putInt(Strings.bottomSize, pxToDp(it.size.component2()))
                    .apply()
            }
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
        BottomNavigationItem(
            selected = currentRoute == Strings.main,
            onClick = { navController.navigate(Strings.main) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowUp,
                    contentDescription = ""
                )
            },
            label = { Text(text = Strings.main) },
            alwaysShowLabel = false,
        )

        BottomNavigationItem(
            selected = currentRoute== Strings.features,
            onClick = { navController.navigate(Strings.features)},
            icon = {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = ""
                )
            },
            label = { Text(text = Strings.features)},
            alwaysShowLabel = false,

            )

        BottomNavigationItem(
            selected = currentRoute == Strings.more,
            onClick = { navController.navigate(Strings.more) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = ""
                )
            },
            label = { Text(text = Strings.more)},
            alwaysShowLabel = false
        )
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Current(route: NavBackStackEntry){
    Crossfade(targetState = route) {
        when (it.arguments?.getString(KEY_ROUTE)) {
            Strings.main -> MainScreen()
            Strings.features -> FeatureScreen()
            Strings.more -> MoreScreen()
        }
    }
}

/*@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Preview
@Composable
fun MainUI(){


    val state = rememberSaveable { mutableStateOf(CurrentScreen.MAIN)}
    Column {
        Current(state = state.value, modifier = Modifier.weight(1f))
        BottomNav(state = state)
    }
}*/

@Preview
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Navigation(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Scaffold(bottomBar = { BottomNav(navController = navController)}) {
            NavHost(
                navController = navController,
                startDestination = Strings.main
            ) {
                composable(Strings.main) {
                    Current(route = navBackStackEntry!!)
                }
                composable(Strings.features) {
                    Current(route = navBackStackEntry!!)
                }
                composable(Strings.more) {
                    Current(route = navBackStackEntry!!)
                }
            }
    }
}

@ExperimentalAnimationApi
@Composable
fun EnterAnimation(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(
            initialOffsetX = { -2000 }
        ) + expandHorizontally(
            expandFrom = Alignment.Start
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut(),
        content = content,
        initiallyVisible = false
    )
}