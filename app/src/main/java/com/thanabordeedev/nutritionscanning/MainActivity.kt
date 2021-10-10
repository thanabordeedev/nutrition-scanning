package com.thanabordeedev.nutritionscanning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.thanabordeedev.nutritionscanning.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    //private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewNewUserBtn.setOnClickListener {
            val intent = Intent(binding.textViewNewUserBtn.context, RegisterActivity::class.java)
            binding.textViewNewUserBtn.context.startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        buttonClicks()
    }

    private fun buttonClicks(){
        binding.textViewLoginBtn.setOnClickListener {
            emptyOrLoginUser()
        }
    }

    private fun emptyOrLoginUser(){
        val email =  binding.TextInputEditEmail.text.toString()
        val password = binding.TextInputEditPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    auth.signInWithEmailAndPassword(email,password).await()

                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,"You are now logged in!",Toast.LENGTH_SHORT).show()
                    }

                    val i = Intent(this@MainActivity,MainMenu::class.java)
                    startActivity(i)
                    finish()

                } catch (e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,"Please input and password again.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun checkIfUserIsLoggedIn(){
        if(auth.currentUser != null){
            val i = Intent(this@MainActivity,MainMenu::class.java)
            startActivity(i)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        checkIfUserIsLoggedIn()
    }

    override fun onBackPressed() {
    }
}