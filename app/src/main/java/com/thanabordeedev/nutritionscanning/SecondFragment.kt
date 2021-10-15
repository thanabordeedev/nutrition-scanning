package com.thanabordeedev.nutritionscanning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.thanabordeedev.nutritionscanning.databinding.FragmentSecondBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(){

    private var _binding: FragmentSecondBinding? = null

    private lateinit var databaseReference:DatabaseReference
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        databaseReference = Firebase.database.reference
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Name_Data")

        binding.textViewNextBtn.setOnClickListener {

            val firstname = binding.TextInputEditFirstnameReg.text.toString()
            val lastname = binding.TextInputEditLastnameReg.text.toString()

            val name = NameData(firstname,lastname)
            if(firstname.isNotEmpty() && lastname.isNotEmpty()){
                if(uid != null){
                    databaseReference.child(uid).setValue(name)

                    findNavController().navigate(R.id.action_SecondFragment_to_ThirdFragment)
                } else {
                    Toast.makeText(this@SecondFragment.context,R.string.text_input_field_all,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}