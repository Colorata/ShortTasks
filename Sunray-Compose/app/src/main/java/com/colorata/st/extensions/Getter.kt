package com.colorata.st.extensions

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.colorata.st.ui.theme.Strings

@Composable
fun getBottomNavigationHeight(): Dp {
    val shared = LocalContext.current.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    return shared.getInt(Strings.bottomSize, 30).dp + getNavBarHeight()
}

@Composable
fun getNavBarHeight(): Dp {
    var navigationBarHeight = 0
    val resourceId: Int = LocalContext.current.resources.getIdentifier(
        "navigation_bar_height",
        "dimen",
        "android"
    )
    if (resourceId > 0) {
        navigationBarHeight = LocalContext.current.resources.getDimensionPixelSize(resourceId)
    }
    return pxToDp(navigationBarHeight).dp
}

@Composable
fun isNewUser(): Boolean{
    val shared = LocalContext.current.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    return shared.getBoolean(Strings.isNewUser, true)
}