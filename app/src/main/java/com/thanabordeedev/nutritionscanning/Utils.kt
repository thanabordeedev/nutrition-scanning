package com.thanabordeedev.nutritionscanning

import com.google.firebase.database.DataSnapshot


fun DataSnapshot.asNameData(): NameData? =
    getValue(NameData::class.java)?.copy(uid = key)

fun DataSnapshot.asDiseasesData(): DiseasesData? =
    getValue(DiseasesData::class.java)?.copy(uid = key)

fun DataSnapshot.asScanResultData(): ScanResultData? =
    getValue(ScanResultData::class.java)?.copy(uid = key)