package com.thanabordeedev.nutritionscanning

import com.google.firebase.database.Exclude

data class ScanResultData(var imagePath: String ?= "",var scanDateTime: String ?= "", var scanResult: String ?= "", var scanDate: String ?= "", var scanTime: String ?= "", @Exclude val uid: String? = ""){
}