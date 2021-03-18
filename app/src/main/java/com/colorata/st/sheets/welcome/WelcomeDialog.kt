package com.colorata.st.sheets.welcome

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.colorata.st.R
import com.colorata.st.extentions.GetTheme
import com.google.android.material.bottomsheet.BottomSheetDialog

class WelcomeDialog(private val context: Context, private val inflater: LayoutInflater, private val shared: SharedPreferences){

    val show = welcome()

    //Fun for the WELCOME
    @SuppressLint("InflateParams")
    private fun welcome(){
        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.welcome_alert, null)

        //Founding BUTTONS
        val text = dialogLayout.findViewById<TextView>(R.id.text_welcome)
        val subText = dialogLayout.findViewById<TextView>(R.id.text_subwelcome)
        val power = dialogLayout.findViewById<Button>(R.id.bubble)
        val bubble = dialogLayout.findViewById<Button>(R.id.power)
        val logo = dialogLayout.findViewById<ImageView>(R.id.image_logo_shorttasks)
        val imageBubble = dialogLayout.findViewById<ImageView>(R.id.image_bubble)

        //Changing COLOR THEME
        power.setTextColor(GetTheme(context).button)
        bubble.setTextColor(GetTheme(context).button)
        text.setTextColor(GetTheme(context).button)
        subText.setTextColor(GetTheme(context).button)

        //Configuring pictures
        //TODO: shared returns null
        if(shared.getBoolean("nightMode", false)){
            logo.setImageResource(R.drawable.ic_logo_main_dark)
            imageBubble.setImageResource(R.drawable.ic_bubble_manager_3_dark)
        }
        //Configuring BACKGROUND
        GetTheme(context).confBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for BUBBLE BUTTON
        bubble.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                FirstBubbleManager(context, inflater, shared).show
            }
        }

        //Click listener for POWER BUTTON
        power.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                FirstPowerAssistant(context, inflater, shared).show
            }
        }
    }
}