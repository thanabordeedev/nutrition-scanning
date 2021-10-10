package com.thanabordeedev.nutritionscanning.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ValueListenerAdapter(val handler: (DataSnapshot) -> Unit): ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        handler(snapshot)
    }

    override fun onCancelled(error: DatabaseError) {

    }
}