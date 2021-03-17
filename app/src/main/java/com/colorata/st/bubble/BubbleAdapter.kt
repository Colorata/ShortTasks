package com.colorata.st.bubble

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.colorata.st.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BubbleAdapter(private val values: MutableList<String>, private val icon: MutableList<Int>, private val context: Context): RecyclerView.Adapter<BubbleAdapter.ViewHolder>() {

    private val shared: SharedPreferences = this.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

    //Fun for getting COUNT OF CONTROLS
    override fun getItemCount() = values.size

    //Creating VIEWHOLDER
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bubble_item, parent, false)
        return ViewHolder(itemView)
    }

    //Init ICON and DESCRIPTION
    class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var textView: TextView? = null
        var fab: FloatingActionButton? = null
        init {
            textView = itemView?.findViewById(R.id.bubble_text)
            fab = itemView?.findViewById(R.id.bubble_button)
        }
    }

    //Putting values to CONTROLS
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textView?.text = values[position]
        holder.textView?.setTextColor(Color.parseColor(shared.getString("buttonColor", "#ffffff")))

        holder.fab?.setImageResource(icon[position])
        holder.fab?.backgroundTintList = ColorStateList.valueOf(Color.parseColor(shared.getString("buttonColor", "#000000")))
        holder.fab?.imageTintList = ColorStateList.valueOf(Color.parseColor(shared.getString("backColor", "#ffffff")))

    }


}