package com.example.expense_sharing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text

class tripadapter(private val triplist: ArrayList<trip>):RecyclerView.Adapter<tripadapter.myViewholder>(){

    private lateinit var mlistener: onitemClicklistener
    interface onitemClicklistener {
        fun onItemClick(position: Int)
    }

    fun setonitemclicklistener(listener: onitemClicklistener){
        mlistener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewholder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.list_item_trip,parent,false)
        return myViewholder(itemview,mlistener)
    }

    override fun onBindViewHolder(holder: myViewholder, position: Int) {
        val currentitem = triplist[position]
        holder.tripname.text = currentitem.name
    }

    override fun getItemCount(): Int {
        return triplist.size
    }
    class myViewholder(itemView:View,listener: onitemClicklistener) : RecyclerView.ViewHolder(itemView) {
        val tripname: TextView = itemView.findViewById(R.id.tvItemTripName)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}