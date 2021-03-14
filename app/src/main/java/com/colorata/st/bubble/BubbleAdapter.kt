package com.colorata.st.bubble

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.colorata.st.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BubbleAdapter(private val values: MutableList<String>, private val icon: MutableList<Int>/*, private val drawable: MutableList<Drawable?> = mutableListOf(null)*/): RecyclerView.Adapter<BubbleAdapter.ViewHolder>() {

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

        holder.fab?.setImageResource(icon[position])

    }


}