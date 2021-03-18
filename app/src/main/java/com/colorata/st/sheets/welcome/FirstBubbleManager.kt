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
import com.colorata.st.extentions.ShowBubble
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bubble_manager_first_alert.view.*

class FirstBubbleManager(private val context: Context, private val inflater: LayoutInflater, private val shared: SharedPreferences) {

    val show = bubbleManagerFirst()

    //Fun for the BUBBLE MANAGER FIRST
    @SuppressLint("InflateParams")
    private fun bubbleManagerFirst(){
        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.bubble_manager_first_alert, null)

        //Founding BUTTONS
        val text = dialogLayout.bubble_manager
        val bubble1 = dialogLayout.bubble_1
        val bubble2 = dialogLayout.bubble_2
        val bubble3 = dialogLayout.bubble_3
        val understand = dialogLayout.cancel_bubble_manager_first
        val enable = dialogLayout.enable_first
        val image1 = dialogLayout.image_bubble_1_first
        val image2 = dialogLayout.image_bubble_2_first
        val image3 = dialogLayout.image_bubble_3_first

        //Changing COLOR THEME
        understand.setTextColor(GetTheme(context).button)
        enable.setTextColor(GetTheme(context).button)
        text.setTextColor(GetTheme(context).button)
        bubble1.setTextColor(GetTheme(context).button)
        bubble2.setTextColor(GetTheme(context).button)
        bubble3.setTextColor(GetTheme(context).button)

        //Configuring pictures
        if(shared.getBoolean("nightMode", false)){
            image1.setImageResource(R.drawable.ic_bubble_manager_1_dark)
            image2.setImageResource(R.drawable.ic_bubble_manager_2_dark)
            image3.setImageResource(R.drawable.ic_bubble_manager_3_dark)
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

        //Click listener for ENABLE BUTTON
        enable.setOnClickListener {
            if(materialBuilder.isShowing){
                ShowBubble(context).bubble
            }
        }
    }
}