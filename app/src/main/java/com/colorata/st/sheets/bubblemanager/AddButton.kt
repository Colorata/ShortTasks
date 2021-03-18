package com.colorata.st.sheets.bubblemanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colorata.st.R
import com.colorata.st.bubble.RecyclerItemClickListener
import com.colorata.st.extentions.GetTheme
import com.colorata.st.extentions.UserButton
import com.colorata.st.userbutton.UserButtonAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

class AddButton(private val context: Context, private val inflater: LayoutInflater) {

    val show = addButton()
    private val shared: SharedPreferences = this.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

    //Fun for BOTTOM SHEET ADD BUTTONS
    @SuppressLint("InflateParams")
    private fun addButton(){

        //TODO: button is 0 but if setTextColor(GetTheme(context).button is normal
        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.add_button_alert, null)

        //Founding BUTTONS AND EDITTEXTS
        val text = dialogLayout.findViewById<TextView>(R.id.text_add_button)
        val appRecycler = dialogLayout.findViewById<RecyclerView>(R.id.app_recycler)
        val cancel = dialogLayout.findViewById<Button>(R.id.cancel_add_button)

        //Changing COLOR THEME
        cancel.setTextColor(GetTheme(context).button)
        text.setTextColor(GetTheme(context).button)

        //Configuring FOR ALL APPS
        val mutableLabels = UserButton(context).labeler
        val mutablePackages = UserButton(context).packager
        val mutableIcons = UserButton(context).iconer

        //Configuring RECYCLERVIEW
        appRecycler.layoutManager = GridLayoutManager(context.applicationContext, 3)
        val bubbleAdapter = UserButtonAdapter(mutableLabels, mutableIcons)
        appRecycler.adapter = bubbleAdapter
        appRecycler.addOnItemTouchListener(
                RecyclerItemClickListener(appRecycler,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            @SuppressLint("WrongConstant")
                            override fun onItemClick(view: View, position: Int) {
                                val editor = shared.edit()
                                editor.putString("userPackage", mutablePackages[position])
                                editor.putString("userLabel", mutableLabels[position])
                                editor.apply()
                                Snackbar.make(view.rootView, "Your control changed to ${mutableLabels[position]}", 1500).apply { view.elevation = 1000F }.show()
                            }
                        }))

        //Configuring BACKGROUND
        GetTheme(context).confBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for CANCEL BUTTON
        cancel.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
            }
        }
    }
}