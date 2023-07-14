package com.dededev.logistics.uiAdmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dededev.logistics.R
import com.dededev.logistics.database.Logistic
import kotlin.math.floor

class NonAdminAdapter(
    private val logisticList: List<Logistic>
) : RecyclerView.Adapter<NonAdminAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_not_editable, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val logistic = logisticList[position]
        return holder.bind(logistic)
    }

    override fun getItemCount(): Int {
        return logisticList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val itemName: TextView = itemView.findViewById(R.id.tv_item_name)
        private val itemValue: TextView = itemView.findViewById(R.id.tv_item_value)

        fun bind(logistic: Logistic) {
            itemName.text = logistic.namaBarang
            itemValue.text = logistic.stokAkhir.toString()
        }
    }
}