package com.thanabordeedev.nutritionscanning

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.core.Context
import com.squareup.picasso.Picasso

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var rImageViewIV: ImageView = itemView.findViewById(R.id.rImageView)
    var scanDataTV: TextView = itemView.findViewById(R.id.scanData)
    var dateDataTV: TextView = itemView.findViewById(R.id.textDate)
    var timeDataTV: TextView = itemView.findViewById(R.id.textTime)


}