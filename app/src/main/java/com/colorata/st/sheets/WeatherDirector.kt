package com.colorata.st.sheets

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.colorata.st.R
import com.colorata.st.extentions.GetTheme
import com.colorata.st.sheets.weatherdirector.ChangeCity
import com.colorata.st.sheets.weatherdirector.FirstChangeCity
import com.google.android.material.bottomsheet.BottomSheetDialog

class WeatherDirector(private val context: Context, private val inflater: LayoutInflater) {

    val show = weatherDirector()
    private val shared: SharedPreferences = this.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

    //Fun for BOTTOM SHEET for WEATHER DIRECTOR
    @SuppressLint("InflateParams")
    private fun weatherDirector(){
        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.weather_director_alert, null)

        //Founding BUTTONS
        val text = dialogLayout.findViewById<TextView>(R.id.text_weather_director)
        val cancel = dialogLayout.findViewById<Button>(R.id.cancel_weather_director)
        val changeCity = dialogLayout.findViewById<Button>(R.id.city_change)

        //Changing COLOR THEME
        cancel.setTextColor(GetTheme(context).button)
        changeCity.setTextColor(GetTheme(context).button)
        text.setTextColor(GetTheme(context).button)

        //Configuring BACKGROUND
        GetTheme(context).confBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for CITY
        changeCity.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                if (shared.getInt("firstChangeCity", 0) < 1) {
                    val editor = shared.edit()
                    editor.putInt("firstChangeCity", shared.getInt("firstChangeCity", 0) + 1)
                    editor.apply()
                    FirstChangeCity(context, inflater).show
                } else{
                    ChangeCity(context, inflater).show
                }
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