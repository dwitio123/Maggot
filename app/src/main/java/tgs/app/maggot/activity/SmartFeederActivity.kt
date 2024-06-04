package tgs.app.maggot.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import tgs.app.maggot.R
import tgs.app.maggot.databinding.ActivitySmartFeederBinding

class SmartFeederActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySmartFeederBinding
    private lateinit var database: DatabaseReference
    private var isOn: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySmartFeederBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        database = FirebaseDatabase.getInstance().reference

        database.child("smartFeeder").child("smartFeeder").get().addOnSuccessListener {
            isOn = it.getValue(Boolean::class.java) ?: false
            updateButton()
        }

        database.child("smartFeeder").child("cadanganMakanan").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.txtPersentase.text = snapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SmartFeederActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        database.child("smartFeeder").child("status").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.tvStatus.text = snapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SmartFeederActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnSmartFeeder.setOnClickListener {
            isOn = !isOn
            database.child("smartFeeder").child("smartFeeder").setValue(isOn)
            updateButton()
            Toast.makeText(this, if (isOn) "Smart Feeder : ON" else "Smart Feeder : OFF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateButton() {
        binding.btnSmartFeeder.background = if (isOn) ResourcesCompat.getDrawable(resources,
            R.drawable.bg_btn_on, null) else ResourcesCompat.getDrawable(resources,
            R.drawable.bg_btn_off, null)
    }
}