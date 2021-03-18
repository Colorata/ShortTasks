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
import kotlinx.android.synthetic.main.first_change_alert.view.*

class FirstChange(private val context: Context, private val inflater: LayoutInflater) {

    val show = firstChange()

    //Fun for the FIRST CHANGE in BUBBLE MANAGER
    @SuppressLint("InflateParams")
    private fun firstChange(){
        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.first_change_alert, null)

        //Founding BUTTONS
        val text = dialogLayout.text_how_change
        val understand = dialogLayout.understand_first_change

        //Changing COLOR THEME
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
                ChangePosition(context, inflater).change
            }
        }
    }
}