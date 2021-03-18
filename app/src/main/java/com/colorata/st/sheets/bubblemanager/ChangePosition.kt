package com.colorata.st.sheets.bubblemanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colorata.st.R
import com.colorata.st.bubble.BubbleAdapter
import com.colorata.st.bubble.RecyclerItemClickListener
import com.colorata.st.extentions.GenerItems
import com.colorata.st.extentions.GetTheme
import com.google.android.material.bottomsheet.BottomSheetDialog

class ChangePosition(private val context: Context, private val inflater: LayoutInflater) {

    private val shared: SharedPreferences = this.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    val change = changePosition()

    private var bufferName: String = ""
    private var bufferIcon: Int = 0
    private var bufferName2: String = ""
    private var bufferIcon2: Int = 0
    private var pos1: Int = 0
    private var pos2: Int = 0
    private var posIs: Boolean = false

    //Fun for BOTTOM SHEET for CHANGE POSITION
    @SuppressLint("InflateParams")
    private fun changePosition(){
        val materialBuilder = BottomSheetDialog(context)
        val dialogLayout: View = inflater.inflate(R.layout.change_controls_alert, null)

        //Founding BUTTONS and RECYCLERVIEW
        val text = dialogLayout.findViewById<TextView>(R.id.text_change_controls)
        val changeRecycler = dialogLayout.findViewById<RecyclerView>(R.id.change_recycler)
        val ok = dialogLayout.findViewById<Button>(R.id.ok_change_position)
        val cancel = dialogLayout.findViewById<Button>(R.id.cancel_change_position)

        //Change COLOR THEME
        ok.setTextColor(GetTheme(context).button)
        cancel.setTextColor(GetTheme(context).button)
        text.setTextColor(GetTheme(context).button)

        //Init values for CONTROLS
        val mutableNames = mutableListOf<String>()
        val mutableIcons = mutableListOf<Int>()

        //Getting LAST INFO about CONTROLS
        for (i in 0..GenerItems().names().lastIndex){
            mutableNames.add(
                    shared.getString(
                            "name $i",
                            GenerItems().names()[i]
                    ).toString()
            )
            mutableIcons.add(shared.getInt("icon $i", GenerItems().icons()[i]))
        }

        //Configuring RECYCLERVIEW
        changeRecycler.layoutManager = GridLayoutManager(context.applicationContext, 3)
        var bubbleAdapter = BubbleAdapter(mutableNames, mutableIcons, context)
        changeRecycler.adapter = bubbleAdapter

        //Configuring BACKGROUND
        GetTheme(context).confBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for CHANGING CONTROLS IN RECYCLERVIEW
        changeRecycler.addOnItemTouchListener(
                RecyclerItemClickListener(changeRecycler,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            @SuppressLint("WrongConstant")
                            override fun onItemClick(view: View, position: Int) {
                                //Checking if FIRST TIME CLICKED
                                if (!posIs) {

                                    //Clicked FIRST TIME
                                    //Remembering WHERE CLICKED
                                    bufferName = mutableNames[position]
                                    bufferIcon = mutableIcons[position]
                                    pos1 = position
                                    posIs = !posIs
                                } else {

                                    //Clicked SECOND TIME
                                    //Remembering WHERE CLICKED
                                    bufferName2 = mutableNames[position]
                                    bufferIcon2 = mutableIcons[position]
                                    pos2 = position

                                    //Changing POSITIONS
                                    mutableNames[pos1] = bufferName2
                                    mutableIcons[pos1] = bufferIcon2
                                    mutableNames[pos2] = bufferName
                                    mutableIcons[pos2] = bufferIcon

                                    //Updating RECYCLERVIEW
                                    bubbleAdapter = BubbleAdapter(mutableNames, mutableIcons, context)
                                    changeRecycler.adapter?.notifyItemChanged(pos1)
                                    changeRecycler.adapter?.notifyItemChanged(pos2)
                                    GenerItems().icons = mutableIcons
                                    GenerItems().names = mutableNames
                                    posIs = !posIs
                                }
                            }
                        }))

        //Click listener for OK BUTTON
        ok.setOnClickListener {
            if(materialBuilder.isShowing) {

                //Putting CONTROLS to SHAREDPREFS
                val editor = shared.edit()
                for (i in 0..mutableNames.lastIndex){
                    editor.putString("name $i", mutableNames[i])
                    editor.putInt("icon $i", mutableIcons[i])
                }
                editor.apply()
                materialBuilder.dismiss()
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