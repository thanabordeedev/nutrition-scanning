package com.thanabordeedev.nutritionscanning

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater

open class LoadingDialog(var myActivity : Activity) {

    private lateinit var dialog: AlertDialog

    fun startLoadingDialog(){
        val builder : AlertDialog.Builder = AlertDialog.Builder(myActivity)

        val inflater : LayoutInflater = myActivity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog,null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog(){
        //test
        dialog.dismiss()
    }
}