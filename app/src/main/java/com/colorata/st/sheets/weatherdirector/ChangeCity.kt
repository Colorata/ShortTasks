package com.colorata.st.sheets.weatherdirector

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.colorata.st.R
import com.colorata.st.extentions.GetTheme
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.change_city_alert.view.*

class ChangeCity(private val context: Context, private val inflaterCity: LayoutInflater) {

    val show = changeCity()
    private val shared: SharedPreferences = this.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

    //Fun for BOTTOM SHEET for CHANGE CITY
    @SuppressLint("InflateParams")
    private fun changeCity(){
        val materialBuilderCity = BottomSheetDialog(context)
        val dialogLayoutCity: View = inflaterCity.inflate(R.layout.change_city_alert, null)

        //Founding BUTTON AND EDITTEXTS for CITY
        val okCity = dialogLayoutCity.ok_change_city
        val cancelCity = dialogLayoutCity.cancel_change_city
        val changeCityField = dialogLayoutCity.city_field_in
        val changeCityFieldLayout = dialogLayoutCity.city_field

        //Changing COLOR THEME
        okCity.setTextColor(GetTheme(context).button)
        cancelCity.setTextColor(GetTheme(context).button)
        changeCityField.setTextColor(GetTheme(context).button)
        changeCityFieldLayout.boxStrokeColor = GetTheme(context).button
        changeCityFieldLayout.hintTextColor = ColorStateList.valueOf(GetTheme(context).button)

        //Configuring BACKGROUND for CITY
        GetTheme(context).confBack(dialogLayoutCity)

        //Showing BOTTOM SHEET for CITY
        materialBuilderCity.setContentView(dialogLayoutCity)
        materialBuilderCity.show()

        //Click listener for OK BUTTON for CITY
        okCity.setOnClickListener {
            if(materialBuilderCity.isShowing) {

                //Checking if EDITTEXT empty
                if (changeCityField.text.toString()!="") {

                    //Putting CITY NAME to SHAREDPREFS
                    val editor = shared.edit()
                    editor.putString("city", changeCityField.text.toString())
                    editor.apply()
                    materialBuilderCity.dismiss()
                } else {

                    //Showing SNACKBAR
                    Snackbar.make(it, "City name isn't entered", 1500).apply { view.elevation = 1000F }.show()
                }
            }
        }

        //Click listener for CANCEL BUTTON for CITY
        cancelCity.setOnClickListener {
            if(materialBuilderCity.isShowing){
                materialBuilderCity.dismiss()
            }
        }
    }
}