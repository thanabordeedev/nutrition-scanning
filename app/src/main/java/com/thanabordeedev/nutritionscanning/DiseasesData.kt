package com.thanabordeedev.nutritionscanning

import com.google.firebase.database.Exclude

data class DiseasesData(var diseaseIndex:String ?= null, @Exclude val uid: String? = "")