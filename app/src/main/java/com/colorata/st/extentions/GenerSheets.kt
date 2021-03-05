package com.colorata.st.extentions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import com.colorata.st.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

class GenerSheets {

    private var versionCounter = 0

    @SuppressLint("InflateParams")
    fun help(context: Context, nightMode: Boolean, inflater: LayoutInflater, applicationContext: Context){
        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.help_alert, null)
        val cancel = dialogLayout.findViewById<Button>(R.id.cancel_help)

        when(nightMode){
            false -> {
                dialogLayout.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.light_background))
            }
            true -> {
                dialogLayout.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.dark_background))
            }
        }

        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        cancel.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
            }
        }
    }
}