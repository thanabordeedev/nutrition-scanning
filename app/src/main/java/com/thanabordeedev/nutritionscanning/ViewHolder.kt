package com.thanabordeedev.nutritionscanning

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var rImageViewIV: ImageView = itemView.findViewById(R.id.rImageView)
    var scanDataTV: TextView = itemView.findViewById(R.id.scanData)
    var dateDataTV: TextView = itemView.findViewById(R.id.textDate2)
    var timeDataTV: TextView = itemView.findViewById(R.id.textTime2)


}