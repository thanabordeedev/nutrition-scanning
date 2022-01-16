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
import android.os.Environment
import android.util.Base64
import android.util.Log
import androidx.core.content.FileProvider
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BitmapFactory

import android.os.ParcelFileDescriptor
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import java.io.FileDescriptor


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
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainMenu?.visibility  = View.VISIBLE
        binding.mainMenuProgressBar?.visibility = View.INVISIBLE
        //land
        binding.mainMenuLand?.visibility  = View.VISIBLE
        binding.mainMenuProgressBarLand?.visibility = View.INVISIBLE

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
        } else {

        }

        //Scan page Intent
        binding.CardViewBtn1.setOnClickListener {
            dispatchTakePictureIntent()
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
            binding.mainMenu?.visibility = View.INVISIBLE
            binding.mainMenuProgressBar?.visibility = View.VISIBLE
            //land
            binding.mainMenuLand?.visibility = View.INVISIBLE
            binding.mainMenuProgressBarLand?.visibility = View.VISIBLE
            var f : File = File(currentPhotoPath)

            val i = Intent(applicationContext,ScanMainActivity::class.java)
            FirebaseStorageManager().uploadImage(Uri.fromFile(f))
            i.putExtra("tempUri",Uri.fromFile(f))


            //run python code here
            mauth = FirebaseAuth.getInstance()
            mDatabase1 = FirebaseDatabase.getInstance().reference
            mDatabase2 = FirebaseDatabase.getInstance().reference
            val uid = mauth.currentUser?.uid

            mDatabase1 = mDatabase1.child("ScanResult_Data").child(uid!!)
            fun diseasesReference(): DatabaseReference = mDatabase2.child("Diseases_Data").child(uid)

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

                                        //detection image
                                        var longtext : String = ""

                                        var fileBaseVisionImage : FirebaseVisionImage = FirebaseVisionImage.fromFilePath(this@MainMenu,
                                            Uri.fromFile(f))
                                        var fileBaseVisionTextDetector : FirebaseVisionTextRecognizer= FirebaseVision.getInstance().cloudTextRecognizer
                                        val scanVisionResult = fileBaseVisionTextDetector.processImage(fileBaseVisionImage).addOnSuccessListener { visionText ->
                                            var blocklist : List<FirebaseVisionText.TextBlock> = visionText.textBlocks
                                            if(blocklist.isEmpty()){
                                                Toast.makeText(this@MainMenu,R.string.no_text_found,Toast.LENGTH_SHORT).show()
                                            } else {
                                                for (block : FirebaseVisionText.TextBlock in visionText.textBlocks ){
                                                    var text : String = block.text
                                                    longtext += text
                                                }

                                                //now i imageString we get encoded image string
                                                var py : Python = Python.getInstance()
                                                var pyObj : PyObject = py.getModule("script")
                                                var obj = pyObj.callAttr("main",longtext,diseaseIndex)

                                                i.putExtra("dr",obj.toString())
                                                startActivity(i)
                                                onBackPressed()
                                                onStop()
                                            }
                                        }.addOnFailureListener { e ->
                                            // Task failed with an exception
                                            // ...
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

    private fun getStringImage(path: Bitmap?): String {
        var sbm : Bitmap? = path?.let { Bitmap.createScaledBitmap(path, it.width/5,it.height/5, true) }
        var baos : ByteArrayOutputStream = ByteArrayOutputStream()
        sbm?.compress(Bitmap.CompressFormat.JPEG,100,baos)
        //store in byte array
        var imageBytes : ByteArray = baos.toByteArray()
        //finally encoded to string
        var encodedImage : String = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return encodedImage
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.thanabordeedev.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, 101)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    /*fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val contentResolver = applicationContext.contentResolver
        val parcelFileDescriptor: ParcelFileDescriptor? = contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image
    }*/

    override fun onBackPressed() {
    }
}


