package com.thanabordeedev.nutritionscanning

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.thanabordeedev.nutritionscanning.databinding.ActivityScanMainBinding
import com.thanabordeedev.nutritionscanning.utils.ValueListenerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class ScanMainActivity : AppCompatActivity() {

    lateinit var binding: ActivityScanMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_main)

        binding = ActivityScanMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val path = intent.getParcelableExtra<Uri>("tempUri")
        binding.surfaceCameraPreview.setImageURI(path)

        val dr = intent.getStringExtra("dr")

        val drSplit = dr?.split("")
        var drText = ""

        drSplit?.forEach {
            if(it == "1"){
                drText = drText + R.string.has_sugar + "\n"
            }
            else if (it == "2"){
                drText = drText + R.string.has_sodium + "\n"
            }
            else if (it == "3"){
                drText = drText + R.string.has_soybean + "\n"
            }
            else if (it == "4"){
                drText = drText + R.string.has_seafood + "\n"
            }
        }

        binding.textCanEatOrNot.text = drText

        //backward to MainMenu
        binding.textViewOkBtn.setOnClickListener {
            val intent = Intent(binding.textViewOkBtn.context,MainMenu::class.java)
            binding.textViewOkBtn.context.startActivity(intent)
            finish()
        }
    }

}


