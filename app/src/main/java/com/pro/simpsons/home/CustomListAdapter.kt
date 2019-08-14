package com.pro.simpsons.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pro.simpsons.OnclickItem
import com.pro.simpsons.R
import com.pro.simpsons.home.models.RelatedTopic

class CustomListAdapter(var relatedTopics: List<RelatedTopic>, var listener: OnclickItem) :
    RecyclerView.Adapter<CustomListAdapter.CustomHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {
        return CustomHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return relatedTopics.size
    }

    override fun onBindViewHolder(holder: CustomHolder, position: Int) {
        holder.text?.let {
            val arrayList = relatedTopics.get(position).Text.split("-")
            it.text = arrayList.get(0)
        }

        holder.text!!.setOnClickListener {
            val arrayList = relatedTopics.get(position).Text.split("-")
            listener.openNextScreen(position)
        }

    }

    fun setData(relatedTopics: List<RelatedTopic>) {
        this.relatedTopics = relatedTopics
        notifyDataSetChanged()
    }

    class CustomHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var text: TextView? = null

        init {
            text = itemView.findViewById(R.id.textView2)
        }

    }
}