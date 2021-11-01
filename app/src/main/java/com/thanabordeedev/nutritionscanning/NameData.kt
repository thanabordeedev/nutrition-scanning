package com.thanabordeedev.nutritionscanning

import com.google.firebase.database.Exclude

data class NameData(val firstName: String = "", val lastName: String = "", @Exclude val uid: String? = "")