package com.thanabordeedev.nutritionscanning

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ScanListAdapter(private val scanHistoryList : ArrayList<ScanResultData>) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.scan_history_list,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = scanHistoryList[position]

        Picasso.get().load(currentItem.imagePath).into(holder.rImageViewIV)
        if(currentItem.scanResult == ""){
            holder.scanDataTV.setText(R.string.text_can_eat_or_not)
        }else{
            holder.scanDataTV.text = currentItem.scanResult
        }
        holder.dateDataTV.text = currentItem.scanDate
        holder.timeDataTV.text = currentItem.scanTime
    }

    override fun getItemCount(): Int {
        return scanHistoryList.size
    }
}