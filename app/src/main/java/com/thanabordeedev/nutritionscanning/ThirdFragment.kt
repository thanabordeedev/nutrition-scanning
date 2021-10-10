package com.thanabordeedev.nutritionscanning

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thanabordeedev.nutritionscanning.databinding.FragmentThirdBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ThirdFragment : Fragment(){

    private var _binding: FragmentThirdBinding? = null

    private lateinit var databaseReference:DatabaseReference
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = FirebaseAuth.getInstance()
        databaseReference = Firebase.database.reference
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Diseases_Data")


        binding.textViewQuestionOkBtn.setOnClickListener {

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

            val diseases = DiseasesData(choice)
            if(uid != null){
                databaseReference.child(uid.toString()).setValue(diseases)

                val intent = Intent(binding.textViewQuestionOkBtn.context, MainMenu::class.java)
                binding.textViewQuestionOkBtn.context.startActivity(intent)

            }

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}