package com.colorata.st.sheets.weatherdirector

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.colorata.st.R
import com.colorata.st.extentions.GetTheme
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.first_change_city_alert.view.*

class FirstChangeCity(private val context: Context, private val inflater: LayoutInflater) {

    val show = firstChangeCity()

    //Fun for the FIRST CHANGE CITY ALERT in WEATHER DIRECTOR
    @SuppressLint("InflateParams")
    private fun firstChangeCity(){
        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.first_change_city_alert, null)

        //Founding BUTTONS
        val subText = dialogLayout.text_tap
        val text = dialogLayout.text_how_change_city
        val understand = dialogLayout.understand_first_change_city_alert

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
                ChangeCity(context, inflater).show
            }
        }
    }

}