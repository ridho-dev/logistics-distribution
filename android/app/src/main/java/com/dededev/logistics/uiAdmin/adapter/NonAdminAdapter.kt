package com.dededev.logistics.uiAdmin.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dededev.logistics.R
import com.dededev.logistics.database.Logistic
import com.dededev.logistics.uiNonAdmin.home.NonAdminViewModel

class NonAdminAdapter(
    private var logisticList: List<Logistic>,
    private val nonAdminViewModel: NonAdminViewModel
) : RecyclerView.Adapter<NonAdminAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val logistic = logisticList[position]
        return holder.bind(logistic)
    }

    override fun getItemCount(): Int {
        return logisticList.size
    }

    fun updateData(newList: List<Logistic>) {
        val diffCallback = LogisticAdapter.DiffCallback(logisticList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        logisticList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val itemName: TextView = itemView.findViewById(R.id.tv_item)
        private val itemValue: TextView = itemView.findViewById(R.id.et_item)

        private var currentItem: Logistic? = null

        init {
            itemValue.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val item = currentItem ?: return
                    val updatedValue = p0?.toString() ?: ""
                    item.stokAkhir = updatedValue.toInt()
                    nonAdminViewModel.update(item)
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
        }

        fun bind(logistic: Logistic) {
            currentItem = logistic
            itemName.text = logistic.namaBarang
            itemValue.text = logistic.stokAkhir.toString()
        }
    }

    class DiffCallback(
        private val oldList: List<Logistic>,
        private val newList: List<Logistic>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}