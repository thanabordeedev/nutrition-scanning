package com.thanabordeedev.nutritionscanning

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater

open class LoadingDialog(var myActivity : Activity) {

    private lateinit var dialog: AlertDialog

    fun startLoadingDialog(){
        var buider : AlertDialog.Builder = AlertDialog.Builder(myActivity)

        var inflater : LayoutInflater = myActivity.layoutInflater
        buider.setView(inflater.inflate(R.layout.custom_dialog,null))
        buider.setCancelable(false)

        dialog = buider.create()
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }
}