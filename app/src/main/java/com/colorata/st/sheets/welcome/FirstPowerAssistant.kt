package com.colorata.st.sheets.welcome

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.colorata.st.R
import com.colorata.st.extentions.GetTheme
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.help_alert.view.*

class FirstPowerAssistant(private val context: Context, private val inflater: LayoutInflater, private val shared: SharedPreferences) {

    val show = powerAssistantFirst()

    //Fun for the POWER ASSISTANT FIRST
    @SuppressLint("InflateParams")
    private fun powerAssistantFirst(){
        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.power_assistant_first_alert, null)

        //Founding BUTTONS
        val power = dialogLayout.findViewById<TextView>(R.id.power_assistant)
        val power1 = dialogLayout.findViewById<TextView>(R.id.power_1)
        val power2 = dialogLayout.findViewById<TextView>(R.id.power_2)
        val power3 = dialogLayout.findViewById<TextView>(R.id.power_3)
        val power4 = dialogLayout.findViewById<TextView>(R.id.power_4)
        val power5 = dialogLayout.findViewById<TextView>(R.id.power_5)
        val power6 = dialogLayout.findViewById<TextView>(R.id.power_6)
        val understand = dialogLayout.findViewById<Button>(R.id.cancel_power_assistant_first)
        val go = dialogLayout.findViewById<Button>(R.id.go_access_first)
        val image1 = dialogLayout.findViewById<ImageView>(R.id.image_power_1_first)
        val image2 = dialogLayout.findViewById<ImageView>(R.id.image_power_2_first)

        //Changing COLOR THEME
        understand.setTextColor(GetTheme(context).button)
        go.setTextColor(GetTheme(context).button)
        power.setTextColor(GetTheme(context).button)
        power1.setTextColor(GetTheme(context).button)
        power2.setTextColor(GetTheme(context).button)
        power3.setTextColor(GetTheme(context).button)
        power4.setTextColor(GetTheme(context).button)
        power5.setTextColor(GetTheme(context).button)
        power6.setTextColor(GetTheme(context).button)

        //Configuring pictures
        if(shared.getBoolean("nightMode", false)){
            image1.setImageResource(R.drawable.ic_power_assistant_1_dark)
            image2.setImageResource(R.drawable.ic_power_assistant_2_dark)
        }

        //Configuring BACKGROUND
        GetTheme(context).confBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for UNDERSTAND BUTTON
        understand.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
            }
        }

        //Click listener for GO BUTTON
        go.setOnClickListener {
            if(materialBuilder.isShowing){
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                context.startActivity(intent)
            }
        }
    }

}