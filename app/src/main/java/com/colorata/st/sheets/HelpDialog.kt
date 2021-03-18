package com.colorata.st.sheets

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.colorata.st.R
import com.colorata.st.extentions.GetTheme
import com.colorata.st.extentions.ShowBubble
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.help_alert.view.*

class HelpDialog(private val context: Context, private val inflater: LayoutInflater) {

    private val shared: SharedPreferences = this.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

    val show = help()

    @SuppressLint("InflateParams")
    private fun help(){
        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.help_alert, null)

        //Founding BUTTONS
        val text = dialogLayout.text_help
        val power = dialogLayout.power_assistant
        val power1 = dialogLayout.power_1
        val power2 = dialogLayout.power_2
        val power3 = dialogLayout.power_3
        val power4 = dialogLayout.power_4
        val power5 = dialogLayout.power_5
        val power6 = dialogLayout.power_6
        val bubble = dialogLayout.bubble_manager
        val bubble1 = dialogLayout.bubble_1
        val bubble2 = dialogLayout.bubble_2
        val bubble3 = dialogLayout.bubble_3

        val cancel = dialogLayout.cancel_help
        val enable = dialogLayout.enable_help
        val goAccess = dialogLayout.go_access

        val imageBubble1 = dialogLayout.image_bubble_1
        val imageBubble2 = dialogLayout.image_bubble_2
        val imageBubble3 = dialogLayout.image_bubble_3
        val imagePower1 = dialogLayout.image_power_1
        val imagePower2 = dialogLayout.image_power_2

        //Changing COLOR THEME
        cancel.setTextColor(GetTheme(context).button)
        enable.setTextColor(GetTheme(context).button)
        goAccess.setTextColor(GetTheme(context).button)
        text.setTextColor(GetTheme(context).button)
        power.setTextColor(GetTheme(context).button)
        power1.setTextColor(GetTheme(context).button)
        power2.setTextColor(GetTheme(context).button)
        power3.setTextColor(GetTheme(context).button)
        power4.setTextColor(GetTheme(context).button)
        power5.setTextColor(GetTheme(context).button)
        power6.setTextColor(GetTheme(context).button)
        bubble.setTextColor(GetTheme(context).button)
        bubble1.setTextColor(GetTheme(context).button)
        bubble2.setTextColor(GetTheme(context).button)
        bubble3.setTextColor(GetTheme(context).button)


        //Configuring pictures
        if(shared.getBoolean("nightMode", false)){
            imagePower1.setImageResource(R.drawable.ic_power_assistant_1_dark)
            imagePower2.setImageResource(R.drawable.ic_power_assistant_2_dark)
            imageBubble1.setImageResource(R.drawable.ic_bubble_manager_1_dark)
            imageBubble2.setImageResource(R.drawable.ic_bubble_manager_2_dark)
            imageBubble3.setImageResource(R.drawable.ic_bubble_manager_3_dark)
        }

        //Configuring BACKGROUND
        GetTheme(context).confBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for ENABLE BUTTON
        enable.setOnClickListener {
            if (materialBuilder.isShowing){
                ShowBubble(context).bubble
            }
        }

        //Click listener for GO BUTTON
        goAccess.setOnClickListener {
            if (materialBuilder.isShowing) {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                context.startActivity(intent)
            }
        }
        //Click listener for CANCEL BUTTON
        cancel.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
            }
        }
    }
}