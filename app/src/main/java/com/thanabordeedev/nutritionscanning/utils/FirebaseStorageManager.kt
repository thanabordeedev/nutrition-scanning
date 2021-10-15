package com.thanabordeedev.nutritionscanning.utils

import android.net.Uri
import android.util.Log
import com.thanabordeedev.nutritionscanning.ScanResultData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class FirebaseStorageManager {
    private val TAG = "FirebaseStorageManager"
    private val mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var mauth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var  maxId : Long = 0

    fun uploadImage(imageURI: Uri){

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val formatterDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val formatterTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val now = Date()
        val filename = formatter.format(now)
        val date = formatterDate.format(now)
        val time = formatterTime.format(now)

        mauth = FirebaseAuth.getInstance()
        databaseReference = Firebase.database.reference
        val uid = mauth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("ScanResult_Data")
        databaseReference = databaseReference.child(uid!!)

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    maxId = (snapshot.childrenCount)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        val uploadTask = mStorageRef.child("images/$uid/$filename.jpg").putFile(imageURI)



        uploadTask.addOnSuccessListener {
            Log.e(TAG,"Image Upload successfully")

            val downloadURLTask = mStorageRef.child("images/$uid/$filename.jpg").downloadUrl

            downloadURLTask.addOnSuccessListener {
                val scanResult = ScanResultData("$it","",date,time)
                databaseReference.child((maxId+1).toString()).setValue(scanResult)

            }

        }.addOnFailureListener{
            Log.e(TAG,"Image Upload failed ${it.printStackTrace()}")
        }
    }



}