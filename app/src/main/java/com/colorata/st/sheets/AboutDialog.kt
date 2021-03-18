package com.colorata.st.sheets

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.colorata.st.R
import com.colorata.st.extentions.GetTheme
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

class AboutDialog(private val context: Context, private val inflater: LayoutInflater) {

    val show = about()
    private var versionCounter = 0

    //Fun for BOTTOM SHEET for ABOUT
    @SuppressLint("InflateParams")
    private fun about(){
        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.about_alert, null)

        //Founding BUTTONS
        val cancel = dialogLayout.findViewById<Button>(R.id.cancel_version)
        val source = dialogLayout.findViewById<Button>(R.id.sourcecode)
        val support = dialogLayout.findViewById<Button>(R.id.support)
        val version = dialogLayout.findViewById<Button>(R.id.version)
        val text = dialogLayout.findViewById<TextView>(R.id.text_about)

        //Changing COLORS FOR ELEMENTS
        cancel.setTextColor(GetTheme(context).button)
        source.setTextColor(GetTheme(context).button)
        support.setTextColor(GetTheme(context).button)
        version.setTextColor(GetTheme(context).button)
        text.setTextColor(GetTheme(context).button)

        //Configuring BACKGROUND
        GetTheme(context).confBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for SOURCE CODE BUTTON
        source.setOnClickListener {
            if (materialBuilder.isShowing) {
                materialBuilder.dismiss()
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.data = Uri.parse("https://github.com/Colorata/ShortTasks")
                context.startActivity(intent)
            }
        }

        //Click listener for SUPPORT US BUTTON
        support.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                val intent1 = Intent()
                intent1.action = Intent.ACTION_VIEW
                intent1.addCategory(Intent.CATEGORY_BROWSABLE)
                intent1.data = Uri.parse("https://qiwi.com/n/COLORATA")
                context.startActivity(intent1)
            }
        }

        //Click listener for VERSION BUTTON
        version.setOnClickListener {
            if(materialBuilder.isShowing){
                //Showing EASTER EGG
                versionCounter += 1
                if (versionCounter == 10) {
                    Snackbar.make(it, "You're pretty", 1500).apply { view.elevation = 1000F }.show()
                }

                else if (versionCounter == 100) {
                    Snackbar.make(it, "Have nothing to do?", 1500).apply { view.elevation = 1000F }.show()
                    versionCounter = 0
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