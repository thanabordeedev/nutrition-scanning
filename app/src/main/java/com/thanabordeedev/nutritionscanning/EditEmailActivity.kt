package com.thanabordeedev.nutritionscanning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.thanabordeedev.nutritionscanning.databinding.ActivityEditEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditEmailBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private var currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_email)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityEditEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        val email = auth.currentUser?.email

        binding.TexInputEditEmail.setText(email)

        binding.textViewConfirmBtn.setOnClickListener {
            emptyOrEditEmail()
        }
    }

    private fun emptyOrEditEmail() {
        val email = binding.TexInputEditEmail.text.toString().trim()

        if(email.isNotEmpty()){
            currentUser?.let { user ->
                user.updateEmail(email)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Toast.makeText(this,R.string.text_successfully_to_updated,Toast.LENGTH_SHORT).show()
                            val i = Intent(this,EditProfileActivity::class.java)
                            startActivity(i)
                            finish()
                        }else{
                            Toast.makeText(this,R.string.text_failed_to_update,Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }else{
            Toast.makeText(this,R.string.text_input_field_all,Toast.LENGTH_SHORT).show()
        }



    }
}