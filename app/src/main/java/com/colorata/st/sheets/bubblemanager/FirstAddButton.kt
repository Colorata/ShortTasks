package com.colorata.st.sheets.bubblemanager

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.colorata.st.R
import com.colorata.st.extentions.GetTheme
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.first_add_button_alert.view.*

class FirstAddButton(private val context: Context, private val inflater: LayoutInflater) {

    val show = firstAdd()

    //Fun for the FIRST CHANGE in BUBBLE MANAGER
    @SuppressLint("InflateParams")
    private fun firstAdd(){
        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.first_add_button_alert, null)

        //Founding BUTTONS
        val text = dialogLayout.text_how_add
        val subText = dialogLayout.text_tap
        val understand = dialogLayout.understand_first_add

        //Changing COLOR THEME
        subText.setTextColor(GetTheme(context).button)
        understand.setTextColor(GetTheme(context).button)
        text.setTextColor(GetTheme(context).button)

        //Configuring BACKGROUND
        GetTheme(context).confBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for CANCEL BUTTON
        understand.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                AddButton(context, inflater).show
            }
        }
    }
}