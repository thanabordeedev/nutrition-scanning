package com.thanabordeedev.nutritionscanning

import android.Manifest
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
import java.io.ByteArrayOutputStream


class MainMenu : AppCompatActivity() {

    private lateinit  var binding:ActivityMainMenuBinding

    private lateinit var mName: NameData
    private lateinit var mauth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

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

            startActivity(i)
            finish()
        }

    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


    override fun onBackPressed() {
    }
}