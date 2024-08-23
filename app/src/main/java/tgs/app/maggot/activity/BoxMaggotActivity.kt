package tgs.app.maggot.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import tgs.app.maggot.R
import tgs.app.maggot.adapter.RiwayatAdapter
import tgs.app.maggot.databinding.ActivityBoxMaggotBinding
import tgs.app.maggot.model.RiwayatData

class BoxMaggotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoxMaggotBinding
    private var isOn: Boolean = false
    private lateinit var recyclerView: RecyclerView
    private lateinit var riwayatAdapter: RiwayatAdapter
    private val riwayatList = mutableListOf<RiwayatData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBoxMaggotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        riwayatAdapter = RiwayatAdapter(riwayatList)
        recyclerView.adapter = riwayatAdapter

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("boxMaggot")

        ref.child("monitoring").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                riwayatList.clear()

                // Iterate through each child under "monitoring"
                for (dataSnapshot in snapshot.children) {
                    val suhu = dataSnapshot.child("suhu").getValue(Double::class.java)
                    val kelembaban = dataSnapshot.child("kelembaban").getValue(Double::class.java)

                    // Create a MonitoringData object
                    val monitoringData = RiwayatData(snapshot.key, suhu, kelembaban)
                    riwayatList.add(monitoringData)
                }

                riwayatAdapter.notifyDataSetChanged()
                // Handle the retrieved data
                for (data in riwayatList) {
                    Log.d("FirebaseData", "Suhu: ${data.suhu}, Kelembaban: ${data.kelembaban}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Failed to read data.", error.toException())
            }
        })

        ref.child("monitoring").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Iterate through each child under "monitoring"
                for (childSnapshot in snapshot.children) {
                    val key = childSnapshot.key
                    val value = childSnapshot.child("suhu").getValue(Double::class.java)
                    Log.d("FirebaseData", "Key: $key, suhu: $value")
                    binding.tvSuhu.text = value.toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Failed to read data.", error.toException())
            }
        })

        ref.child("monitoring").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Iterate through each child under "monitoring"
                for (childSnapshot in snapshot.children) {
                    val key = childSnapshot.key
                    val value = childSnapshot.child("kelembaban").getValue(Double::class.java)
                    Log.d("FirebaseData", "Key: $key, kelembaban: $value")
                    binding.tvKelembaban.text = value.toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Failed to read data.", error.toException())
            }
        })

        ref.child("status").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.txtKondisi.text = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BoxMaggotActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        ref.child("kipas").get().addOnSuccessListener {
            isOn = it.getValue(Boolean::class.java) ?: false
            if (isOn) binding.txtKipas.text = "Kipas menyala" else "Kipas mati"
            updateButton()
        }

        binding.btnKipas.setOnClickListener {
            isOn = !isOn
            ref.child("kipas").setValue(isOn)
            updateButton()
            Toast.makeText(this, if (isOn) "Kipas : ON" else "Kipas : OFF", Toast.LENGTH_SHORT).show()
            if (isOn) { binding.txtKipas.text = "Kipas menyala" } else { binding.txtKipas.text = "Kipas mati" }
        }
    }

    private fun updateButton() {
        binding.btnKipas.background = if (isOn) ResourcesCompat.getDrawable(resources,
            R.drawable.bg_btn_on, null) else ResourcesCompat.getDrawable(resources,
            R.drawable.bg_btn_off, null)
    }
}