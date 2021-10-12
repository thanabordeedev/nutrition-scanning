package com.thanabordeedev.nutritionscanning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.thanabordeedev.nutritionscanning.databinding.ActivityScanHistoryBinding
import java.lang.reflect.Member

class ScanHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanHistoryBinding
    private lateinit var mRecylerView: RecyclerView
    private lateinit var mauth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityScanHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mauth = FirebaseAuth.getInstance()

        mRecylerView = binding.recyclerview
        mRecylerView.setHasFixedSize(true)

        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseRef = firebaseDatabase.getReference("ScanResult_Data").child(mauth.currentUser!!.uid)
    }

    override fun onStart() {
        super.onStart()

        var firebaseRecycleAdapter: FirebaseRecyclerAdapter<Member,ViewHolder>
    }
}