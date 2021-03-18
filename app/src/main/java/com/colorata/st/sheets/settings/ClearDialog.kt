package com.colorata.st.sheets.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.colorata.st.R
import com.colorata.st.extentions.GetTheme
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.clear_data_allowing_alert.view.*

class ClearDialog(private val context: Context, private val inflater: LayoutInflater) {

    private val shared: SharedPreferences = this.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

    //Fun for the CLEAR DATA
    @SuppressLint("InflateParams")
    fun clearData(){
        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.clear_data_allowing_alert, null)

        //Founding BUTTONS
        val text = dialogLayout.text_allow
        val subText = dialogLayout.text_clear
        val ok = dialogLayout.ok_clear
        val cancel = dialogLayout.cancel_clear

        //Changing COLOR THEME
        ok.setTextColor(GetTheme(context).button)
        cancel.setTextColor(GetTheme(context).button)
        text.setTextColor(GetTheme(context).button)
        subText.setTextColor(GetTheme(context).button)

        //Configuring BACKGROUND
        GetTheme(context).confBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for UNDERSTAND BUTTON
        ok.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                shared.edit().clear().apply()
            }
        }

        //Click listener for GO BUTTON
        cancel.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
            }
        }
    }
}