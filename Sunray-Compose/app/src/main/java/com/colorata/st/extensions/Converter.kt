package com.colorata.st.extensions

import android.content.res.Resources


fun pxToDp(px: Int) = (px / Resources.getSystem().displayMetrics.density).toInt()