package com.colorata.st.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class Dimens(val sp: TextUnit = 0.sp, val dp: Dp = 0.dp) {
    HELP_TITLE(sp = 16.sp),
    CARD_TITLE(sp = 24.sp),
    SPLASH_TITLE(sp = 18.sp),
    LOGO_PADDING(dp = 90.dp)
}