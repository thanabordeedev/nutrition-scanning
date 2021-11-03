package com.thanabordeedev.nutritionscanning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.thanabordeedev.nutritionscanning.databinding.ActivityScanHistoryBinding

class ScanHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanHistoryBinding
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var scanArrayList : ArrayList<ScanResultData>
    private var maxId : Long = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityScanHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mRecyclerView = findViewById(R.id.recyclerview)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.setHasFixedSize(true)

        scanArrayList = arrayListOf<ScanResultData>()
        getScanData()

    }

    private fun getScanData() {
        mAuth = FirebaseAuth.getInstance()
        firebaseRef = FirebaseDatabase.getInstance().getReference("ScanResult_Data").child(mAuth.currentUser!!.uid)

        firebaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    maxId = (snapshot.childrenCount)

                    if(maxId > 0){
                        binding.dataNotFound.visibility = View.INVISIBLE
                    }
                    for(scanDataSnapshot in snapshot.children){
                        val num = scanDataSnapshot.getValue(ScanResultData::class.java)
                        scanArrayList.add(num!!)
                    }
                    mRecyclerView.adapter = ScanListAdapter(scanArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}

