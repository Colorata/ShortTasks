package com.colorata.st.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.colorata.st.extensions.getNavBarHeight
import com.colorata.st.extensions.pxToDp
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.SuperStore
import com.colorata.st.ui.theme.backgroundColor
import com.colorata.st.ui.theme.foregroundColor

@ExperimentalAnimationApi
@Composable
fun BottomNav(navController: NavController) {

    val context = LocalContext.current

    val height = getNavBarHeight()
    BottomNavigation(
        contentColor = foregroundColor(),
        backgroundColor = backgroundColor(),
        modifier = Modifier
            .padding(bottom = height)
            .onGloballyPositioned {
                SuperStore(context).drop(
                    Strings.bottomSize,
                    pxToDp(it.size.component2()).toInt() + height.value.toInt()
                )
            }
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        BottomNavigationItem(
            selected = currentRoute == Strings.main,
            onClick = { navController.navigate(Strings.main) { launchSingleTop = true } },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowUp,
                    contentDescription = ""
                )
            },
            label = { Text(text = Strings.main) },
            alwaysShowLabel = false
        )

        BottomNavigationItem(
            selected = currentRoute == Strings.features,
            onClick = { navController.navigate(Strings.features) { launchSingleTop = true } },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = ""
                )
            },
            label = { Text(text = Strings.features) },
            alwaysShowLabel = false
        )

        BottomNavigationItem(
            selected = currentRoute == Strings.more,
            onClick = { navController.navigate(Strings.more) { launchSingleTop = true } },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = ""
                )
            },
            label = { Text(text = Strings.more) },
            alwaysShowLabel = false
        )
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun Current(route: NavBackStackEntry) {
    Crossfade(targetState = route) {
        when (it.destination.route) {
            Strings.main -> MainScreen()
            Strings.features -> FeatureScreen()
            Strings.more -> MoreScreen()
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Scaffold(
        bottomBar = { BottomNav(navController = navController) },
        backgroundColor = Color.Transparent
    ) {
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