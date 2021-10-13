package com.thanabordeedev.nutritionscanning

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.thanabordeedev.nutritionscanning.databinding.ActivityScanMainBinding
import com.thanabordeedev.nutritionscanning.utils.ValueListenerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.ByteArrayOutputStream

class ScanMainActivity : AppCompatActivity() {

    lateinit var binding: ActivityScanMainBinding
    private lateinit var mScanResult: ScanResultData
    private lateinit var mDiseasesData: DiseasesData
    private lateinit var mauth: FirebaseAuth
    private lateinit var mDatabase1: DatabaseReference
    private lateinit var mDatabase2: DatabaseReference
    private var maxId : Long = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_main)

        binding = ActivityScanMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var path = intent.getParcelableExtra<Bitmap>("tempUri")
        binding.surfaceCameraPreview.setImageBitmap(path)

        mauth = FirebaseAuth.getInstance()
        mDatabase1 = FirebaseDatabase.getInstance().reference
        mDatabase2 = FirebaseDatabase.getInstance().reference
        val uid = mauth.currentUser?.uid
        mDatabase1 = mDatabase1.child("ScanResult_Data").child(uid!!)
        fun diseasesReference(): DatabaseReference = mDatabase2.child("Diseases_Data")

        //Python Script
        if(!Python.isStarted())
            Python.start(AndroidPlatform(this))

        mDatabase1.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    maxId = (snapshot.childrenCount)

                    fun scanResultReference(): DatabaseReference = mDatabase1.child(maxId.toString())

                    scanResultReference().addListenerForSingleValueEvent(
                        ValueListenerAdapter{
                            mScanResult = it.asScanResultData()!!
                            val pic = mScanResult.imagePath

                            diseasesReference().addListenerForSingleValueEvent(
                                ValueListenerAdapter{
                                    mDiseasesData = it.asDiseasesData()!!

                                    var py : Python = Python.getInstance()
                                    //var pyObj : PyObject = py.getModule("script")
                                    //var obj = pyObj.callAttr("main",mScanResult.imagePath,mDiseasesData.diseaseIndex)
                                    //Log.e("Test Result",obj.toString())
                                }
                            )
                        }
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        //backward to MainMenu
        binding.textViewOkBtn.setOnClickListener {
            val intent = Intent(binding.textViewOkBtn.context,MainMenu::class.java)
            binding.textViewOkBtn.context.startActivity(intent)
            finish()
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }



}


