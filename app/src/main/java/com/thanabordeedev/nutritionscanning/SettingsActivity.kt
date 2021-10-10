package com.thanabordeedev.nutritionscanning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.thanabordeedev.nutritionscanning.databinding.ActivitySettingsBinding
import com.thanabordeedev.nutritionscanning.utils.ValueListenerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private lateinit var mName: NameData
    private lateinit var mauth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mauth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        fun currentUserReference(): DatabaseReference =
            mDatabase.child("Name_Data").child(mauth.currentUser!!.uid)

        currentUserReference().addListenerForSingleValueEvent(
            ValueListenerAdapter{
                mName = it.asNameData()!!
                binding.TextViewName.text = mName.firstName + " " + mName.lastName
            }
        )

        //logout
        binding.CardViewLogout.setOnClickListener {
            signOutUser()
        }

        //edit profile
        binding.CardViewEditProfile.setOnClickListener {
            val i = Intent(binding.CardViewEditProfile.context,EditProfileActivity::class.java)
            startActivity(i)
        }

        //edit Password
        binding.CardViewEditPassword.setOnClickListener {
            val i = Intent(binding.CardViewEditPassword.context,EditPasswordActivity::class.java)
            startActivity(i)
        }

        //edit Diseases
        binding.CardViewEditDiseases.setOnClickListener {
            val i = Intent(binding.CardViewEditDiseases.context,EditDiseasesActivity::class.java)
            startActivity(i)
        }
    }

    private fun signOutUser(){
        mauth.signOut()

        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()

        Toast.makeText(this,"Successfully signed out!", Toast.LENGTH_SHORT).show()
    }
}