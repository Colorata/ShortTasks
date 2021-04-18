package com.colorata.st.screens


import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.colorata.st.extensions.getNavBarHeight
import com.colorata.st.extensions.pxToDp
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.backgroundColor
import com.colorata.st.ui.theme.foregroundColor

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

@ExperimentalAnimationApi
@Composable
fun BottomNav(navController: NavController){

    val shared = LocalContext.current.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)

    /*var showMain by remember {
        mutableStateOf(true)
    }

    var showFeatures by remember {
        mutableStateOf(false)
    }

    var showMore by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier.padding(SDimens.smallPadding, bottom = 50.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable {
                showMain = true
                showFeatures = false
                showMore = false
                navController.navigate(Strings.main)
            }
        ) {
            Image(
                imageVector = Icons.Outlined.KeyboardArrowUp,
                contentDescription = ""
            )
            AnimatedVisibility(visible = showMain) {
                Text(text = Strings.main)
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable {
                showMain = false
                showFeatures = true
                showMore = false
                navController.navigate(Strings.features)
            }
        ) {
            Image(
                imageVector = Icons.Outlined.AddCircle,
                contentDescription = ""
            )
            AnimatedVisibility(visible = showFeatures) {
                Text(text = Strings.features)
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable {
                showMain = false
                showFeatures = false
                showMore = true
                navController.navigate(Strings.more)
            }
        ) {
            Image(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = ""
            )
            AnimatedVisibility(visible = showMore) {
                Text(text = Strings.more)
            }
        }
    }
*/

    BottomNavigation(
        contentColor = foregroundColor(),
        backgroundColor = backgroundColor(),
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
            onClick = { navController.navigate(Strings.main){ launchSingleTop = true } },
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
            selected = currentRoute== Strings.features,
            onClick = { navController.navigate(Strings.features){ launchSingleTop = true } },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = ""
                )
            },
            label = { Text(text = Strings.features)},
            alwaysShowLabel = false
            )

        BottomNavigationItem(
            selected = currentRoute == Strings.more,
            onClick = { navController.navigate(Strings.more){ launchSingleTop = true } },
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

