package com.colorata.st.userbutton

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.colorata.st.R

class UserButtonAdapter(private val values: MutableList<String>, private val drawable: MutableList<Drawable?> = mutableListOf(null)): RecyclerView.Adapter<UserButtonAdapter.ViewHolder>() {

    //Fun for getting COUNT OF APPS
    override fun getItemCount() = values.size

    //Creating VIEWHOLDER
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_button_change_item, parent, false)
        return ViewHolder(itemView)
    }

    //Init ICON and DESCRIPTION
    class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var textView: TextView? = null
        var icon: ImageView? = null
        init {
            textView = itemView?.findViewById(R.id.app_text)
            icon = itemView?.findViewById(R.id.app_button)
        }
    }

    //Putting values to APPS
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textView?.text = values[position]
        holder.icon?.setImageDrawable(drawable[position])

    }


}