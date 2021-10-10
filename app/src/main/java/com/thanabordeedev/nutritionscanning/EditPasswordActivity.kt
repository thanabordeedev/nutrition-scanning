package com.thanabordeedev.nutritionscanning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.thanabordeedev.nutritionscanning.databinding.ActivityEditPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPasswordBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private var currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        binding.textViewConfirmBtn.setOnClickListener {
            emptyOrEditPassword()
        }
    }

    private fun emptyOrEditPassword() {
        val password = binding.textViewPasswordEdit.text.toString().trim()

        if(password.isNotEmpty()){
            currentUser?.let { user ->
                user.updatePassword(password)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Toast.makeText(this,R.string.text_successfully_to_updated,Toast.LENGTH_SHORT).show()
                            val i = Intent(this,SettingsActivity::class.java)
                            startActivity(i)
                            finish()
                        }else{
                            Toast.makeText(this,R.string.text_failed_to_update,Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }else{
            Toast.makeText(this,R.string.text_input_field_all, Toast.LENGTH_SHORT).show()
        }
    }
}