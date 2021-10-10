package com.thanabordeedev.nutritionscanning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thanabordeedev.nutritionscanning.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Edit Username
        binding.CardViewEditUsername.setOnClickListener {
            val i = Intent(binding.CardViewEditUsername.context,EditUsernameActivity::class.java)
            startActivity(i)
        }

        //edit Email
        binding.CardViewEditEmail.setOnClickListener {
            val i = Intent(binding.CardViewEditEmail.context,EditEmailActivity::class.java)
            startActivity(i)
        }
    }
}