package com.tanzentlab.checksamfirm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tanzentlab.checksamfirm.R
import com.tanzentlab.checksamfirm.dialogs.Options

class OptionsAdapter(private val optionList: List<Options>): RecyclerView.Adapter<OptionsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.image)
        var text: TextView = view.findViewById(R.id.text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_items, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val options = optionList[position]
        holder.image.setImageResource(options.imageResource)
        holder.text.text = options.options
    }

    override fun getItemCount(): Int {
        return optionList.size
    }
}