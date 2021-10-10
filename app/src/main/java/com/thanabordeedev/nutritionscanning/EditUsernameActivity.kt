package com.thanabordeedev.nutritionscanning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.thanabordeedev.nutritionscanning.databinding.ActivityEditUsernameBinding
import com.thanabordeedev.nutritionscanning.utils.ValueListenerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditUsernameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditUsernameBinding
    private lateinit var mName: NameData
    private lateinit var mauth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_username)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityEditUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mauth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        fun currentUserReference(): DatabaseReference =
            mDatabase.child("Name_Data").child(mauth.currentUser!!.uid)

        currentUserReference().addListenerForSingleValueEvent(
            ValueListenerAdapter{
                mName = it.asNameData()!!
                binding.TexInputEditFirstname.setText(mName.firstName)
                binding.TextInputEditLastname.setText(mName.lastName)
            }
        )

        binding.textViewConfirmBtn.setOnClickListener {
            val firstName = binding.TexInputEditFirstname.text.toString()
            val lastName = binding.TextInputEditLastname.text.toString()


            if(firstName.isEmpty() && lastName.isEmpty()){
                Toast.makeText(this,R.string.text_input_field_all,Toast.LENGTH_SHORT).show()
            }else{
                updateData(firstName,lastName)
            }
        }
    }

    private fun updateData(firstName: String, lastName: String) {
        mDatabase = FirebaseDatabase.getInstance().getReference("Name_Data")
        val nameData = mapOf<String,String>(
            "firstName" to firstName,
            "lastName" to lastName
        )

        mDatabase.child(mauth.currentUser!!.uid).updateChildren(nameData).addOnSuccessListener {
            Toast.makeText(this,R.string.text_successfully_to_updated,Toast.LENGTH_SHORT).show()
            val i = Intent(this,EditProfileActivity::class.java)
            startActivity(i)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this,R.string.text_failed_to_update,Toast.LENGTH_SHORT).show()
        }
    }

}