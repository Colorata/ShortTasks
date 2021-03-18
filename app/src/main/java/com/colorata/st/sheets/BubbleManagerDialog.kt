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
import com.colorata.st.extentions.ShowBubble
import com.colorata.st.sheets.bubblemanager.AddButton
import com.colorata.st.sheets.bubblemanager.ChangePosition
import com.colorata.st.sheets.bubblemanager.FirstAddButton
import com.colorata.st.sheets.bubblemanager.FirstChange
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bubble_manager_alert.view.*

class BubbleManagerDialog(private val context: Context, private val inflater: LayoutInflater) {

    private val shared: SharedPreferences = this.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    val show = bubbleManager()

    //Fun BOTTOM SHEET for BUBBLE MANAGER
    @SuppressLint("InflateParams")
    private fun bubbleManager(){

        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.bubble_manager_alert, null)

        //Founding BUTTONS
        val text = dialogLayout.text_bubble_manager
        val cancel = dialogLayout.cancel_bubble_manager
        val enable = dialogLayout.enable
        val changePosition = dialogLayout.change_position
        val addButton = dialogLayout.add_button

        //Changing COLOR THEME
        cancel.setTextColor(GetTheme(context).button)
        enable.setTextColor(GetTheme(context).button)
        changePosition.setTextColor(GetTheme(context).button)
        addButton.setTextColor(GetTheme(context).button)
        text.setTextColor(GetTheme(context).button)

        //Configuring BACKGROUND
        GetTheme(context).confBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for ENABLE BUBBLE BUTTOn
        enable.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                //Showing BUBBLE
                ShowBubble(context).bubble
            }
        }

        //Click listener for CHANGE POSITION
        changePosition.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                if (shared.getInt("firstChange", 0) < 1) {
                    val editor = shared.edit()
                    editor.putInt("firstChange", shared.getInt("firstChange", 0) + 1)
                    editor.apply()
                    FirstChange(context, inflater).show
                } else {
                    ChangePosition(context, inflater).change
                }
            }
        }

        //Click listener for ADD BUTTON
        addButton.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                if (shared.getInt("firstAddButton", 0) < 1) {
                    val editor = shared.edit()
                    editor.putInt("firstAddButton", shared.getInt("firstAddButton", 0) + 1)
                    editor.apply()
                    FirstAddButton(context, inflater).show
                } else {
                    AddButton(context, inflater).show
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