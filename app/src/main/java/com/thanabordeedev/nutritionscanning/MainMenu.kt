package com.thanabordeedev.nutritionscanning

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.thanabordeedev.nutritionscanning.databinding.ActivityMainMenuBinding
import com.thanabordeedev.nutritionscanning.utils.FirebaseStorageManager
import com.thanabordeedev.nutritionscanning.utils.ValueListenerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.content.Context
import android.util.Base64
import android.util.Log
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import java.io.ByteArrayOutputStream

class MainMenu : AppCompatActivity() {

    private lateinit  var binding:ActivityMainMenuBinding

    private lateinit var mName: NameData
    private lateinit var mauth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    private lateinit var mScanResult: ScanResultData
    private lateinit var mDiseasesData: DiseasesData
    private lateinit var mDatabase1: DatabaseReference
    private lateinit var mDatabase2: DatabaseReference

    private var maxId : Long = 1
    private var imageString = ""
    lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
        } else {

        }

        //Scan page Intent
        binding.CardViewBtn1.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,101)
        }

        binding.CardViewBtn2.setOnClickListener {
            val intent = Intent(this,ScanHistoryActivity::class.java)
            startActivity(intent)
        }

        mauth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        fun currentUserReference(): DatabaseReference =
            mDatabase.child("Name_Data").child(mauth.currentUser!!.uid)

        currentUserReference().addListenerForSingleValueEvent(
            ValueListenerAdapter{
                mName = it.asNameData()!!
                binding.TextViewWelcome2.text = mName.firstName + " " + mName.lastName
            }
            )

        btnClicked()

    }

    private fun btnClicked(){
        binding.CardViewBtn3.setOnClickListener {
            val i = Intent(binding.CardViewBtn3.context,SettingsActivity::class.java)
            binding.CardViewBtn3.context.startActivity(i)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

        } else {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            val photo: Bitmap? = data?.getParcelableExtra<Bitmap>("data")

            val tempUri: Uri? = photo?.let { getImageUri(this, it) }
            val i = Intent(applicationContext,ScanMainActivity::class.java)

            if (tempUri != null) {
                FirebaseStorageManager().uploadImage(tempUri)
            }
            i.putExtra("tempUri",photo)

            //run python code here

            mauth = FirebaseAuth.getInstance()
            mDatabase1 = FirebaseDatabase.getInstance().reference
            mDatabase2 = FirebaseDatabase.getInstance().reference
            val uid = mauth.currentUser?.uid

            mDatabase1 = mDatabase1.child("ScanResult_Data").child(uid!!)
            fun diseasesReference(): DatabaseReference = mDatabase2.child("Diseases_Data").child(uid!!)

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

                                diseasesReference().addListenerForSingleValueEvent(
                                    ValueListenerAdapter{
                                        mDiseasesData = it.asDiseasesData()!!
                                        val diseaseIndex = mDiseasesData.diseaseIndex
                                        if (diseaseIndex != null) {
                                            Log.e("test",diseaseIndex)
                                        }

                                        imageString = getStringImage(photo)

                                        //now i imageString we get encoded image string
                                        var py : Python = Python.getInstance()
                                        var pyObj : PyObject = py.getModule("script")
                                        var obj = pyObj.callAttr("main",imageString,mDiseasesData.diseaseIndex)
                                        if(obj.toString() != ""){
                                            startActivity(i)
                                            Log.e("Test Result",obj.toString())
                                        }

                                    }
                                )
                            }
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getStringImage(path: Bitmap?): String {
        var baos : ByteArrayOutputStream = ByteArrayOutputStream()
        path?.compress(Bitmap.CompressFormat.JPEG,100,baos)
        //store in byte array
        var imageBytes : ByteArray = baos.toByteArray()
        //finally encoded to string
        var encodedImage : String = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return encodedImage
    }

    override fun onBackPressed() {
    }
}