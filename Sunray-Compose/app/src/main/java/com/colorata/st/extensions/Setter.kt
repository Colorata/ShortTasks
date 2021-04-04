package com.colorata.st.extensions

import android.content.Context
import android.content.Intent
import com.colorata.st.CurrentScreen
import com.colorata.st.activities.SecondaryActivity
import com.colorata.st.ui.theme.Strings

fun setIsNewUser(context: Context, new: Boolean){
    val shared = context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    shared.edit().putBoolean(Strings.isNewUser, new).apply()
}

fun goToSecondary(context: Context, screen: CurrentScreen){
    val intent = Intent(context, SecondaryActivity::class.java)
    intent.putExtra(Strings.screen, screen)
    context.startActivity(intent)
}