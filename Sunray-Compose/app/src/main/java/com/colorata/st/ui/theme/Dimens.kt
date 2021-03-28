package com.colorata.st.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.solver.state.Dimension

enum class Dimens(val sp: TextUnit = 0.sp, val dp: Dp = 0.dp) {
    TITLE(sp = 18.sp),
    LOGO_PADDING(dp = 90.dp)
}