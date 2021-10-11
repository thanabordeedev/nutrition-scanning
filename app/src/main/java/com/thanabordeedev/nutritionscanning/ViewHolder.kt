package com.thanabordeedev.nutritionscanning

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.core.Context

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var rImageViewIV: ImageView
    private lateinit var scanDataTV: TextView
    private lateinit var dateDataTV: TextView
    private lateinit var timeDataTV: TextView

    fun setDetail(content: Context,imagePath: String,scanData: String,scanDate: String, scanTime: String){

        rImageViewIV = itemView.findViewById(R.id.rImageView)
        scanDataTV = itemView.findViewById(R.id.scanData)
        dateDataTV = itemView.findViewById(R.id.textDate)
        timeDataTV = itemView.findViewById(R.id.textTime)


        scanDataTV.setText(scanData)
        dateDataTV.text = "${R.string.date2} $scanDataTV"
        timeDataTV.text = "${R.string.time2} $timeDataTV"

    }
}