package com.colorata.st.sheets

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import com.colorata.st.R
import com.colorata.st.extentions.GetTheme
import com.colorata.st.sheets.settings.ClearDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.settings_alert.view.*

class SettingsDialog(private val context: Context, private val inflater: LayoutInflater) {

    val show = settings()
    //Fun for BOTTOM SHEET SETTINGS
    @SuppressLint("InflateParams")
    fun settings(){
        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.settings_alert, null)

        //Founding BUTTONS
        val text = dialogLayout.text_settings
        val cancel = dialogLayout.cancel_settings
        val settings = dialogLayout.settings_app
        val clear = dialogLayout.clear_data

        //Changing COLOR THEME
        cancel.setTextColor(GetTheme(context).button)
        settings.setTextColor(GetTheme(context).button)
        clear.setTextColor(GetTheme(context).button)
        text.setTextColor(GetTheme(context).button)

        //Configuring BACKGROUND
        GetTheme(context).confBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for APP SETTINGS BUTTON
        settings.setOnClickListener {
            if (materialBuilder.isShowing) {
                materialBuilder.dismiss()

                //Going to SYSTEM SETTINGS
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
        }

        //Click listener for CLEAR BUTTON
        clear.setOnClickListener {
            if (materialBuilder.isShowing){
                materialBuilder.dismiss()
                ClearDialog(context, inflater).clearData()
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