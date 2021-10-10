package com.thanabordeedev.nutritionscanning

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.thanabordeedev.nutritionscanning.databinding.FragmentFirstBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewCancelBtn.setOnClickListener {
            val intent = Intent(binding.textViewCancelBtn.context, MainActivity::class.java)
            binding.textViewCancelBtn.context.startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()

        binding.textViewCreateAccountBtn.setOnClickListener {
            emptyOrRegisterUser()

        }
    }

    private fun emptyOrRegisterUser(){
        val email = binding.TextInputEditEmailReg.text.toString()
        val password = binding.TextInputEditPasswordReg.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){

            GlobalScope.launch(Dispatchers.IO) {
                try {

                    auth.createUserWithEmailAndPassword(email,password).await()

                    withContext(Dispatchers.Main){
                        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                    }

                } catch (e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@FirstFragment.context,R.string.text_input_field_all,Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}