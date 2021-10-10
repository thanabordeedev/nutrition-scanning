package com.thanabordeedev.nutritionscanning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.thanabordeedev.nutritionscanning.databinding.ActivityEditDiseasesBinding
import com.thanabordeedev.nutritionscanning.utils.ValueListenerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditDiseasesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDiseasesBinding
    private lateinit var mDiseases: DiseasesData
    private lateinit var mauth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_diseases)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityEditDiseasesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mauth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        fun currentUserReference(): DatabaseReference =
            mDatabase.child("Diseases_Data").child(mauth.currentUser!!.uid)

        currentUserReference().addListenerForSingleValueEvent(
            ValueListenerAdapter{
                mDiseases = it.asDiseasesData()!!
                val diseaseIndex = mDiseases.diseaseIndex
                val diseases = diseaseIndex?.split("")

                if (diseases != null) {
                    for(disease in diseases){
                        if(disease == "1"){
                            binding.TextViewQuestionChb1.isChecked = true
                        }else if(disease == "2"){
                            binding.TextViewQuestionChb2.isChecked = true
                        }else if(disease == "3"){
                            binding.TextViewQuestionChb3.isChecked = true
                        }else if(disease == "4"){
                            binding.TextViewQuestionChb4.isChecked = true
                        }
                    }
                }


            }
        )

        //confirm
        binding.textViewConfirmBtn.setOnClickListener {
            var choice = ""
            if(binding.TextViewQuestionChb1.isChecked){
                choice += 1
            }
            if(binding.TextViewQuestionChb2.isChecked){
                choice += 2
            }
            if(binding.TextViewQuestionChb3.isChecked){
                choice += 3
            }
            if(binding.TextViewQuestionChb4.isChecked){
                choice += 4
            }
            updateData(choice)
        }
    }

    private fun updateData(diseaseIndex: String) {
        mDatabase = FirebaseDatabase.getInstance().getReference("Diseases_Data")
        val diseaseData = mapOf<String,String>(
            "diseaseIndex" to diseaseIndex
        )

        mDatabase.child(mauth.currentUser!!.uid).updateChildren(diseaseData).addOnSuccessListener {
            Toast.makeText(this,R.string.text_successfully_to_updated, Toast.LENGTH_SHORT).show()
            val i = Intent(this,SettingsActivity::class.java)
            startActivity(i)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this,R.string.text_failed_to_update,Toast.LENGTH_SHORT).show()
        }
    }
}