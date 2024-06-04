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
import tgs.app.maggot.databinding.ActivityRuangBudidayaBinding

class RuangBudidayaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRuangBudidayaBinding
    private lateinit var database: DatabaseReference
    private var isOn: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRuangBudidayaBinding.inflate(layoutInflater)
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

        database.child("ruangBudidaya").child("suhu").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val suhu = snapshot.getValue(Int::class.java) ?: 0
                binding.tvSuhu.text = suhu.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RuangBudidayaActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        database.child("ruangBudidaya").child("kelembaban").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val kelembaban = snapshot.getValue(Int::class.java) ?: 0
                binding.tvKelembaban.text = kelembaban.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RuangBudidayaActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        database.child("ruangBudidaya").child("kipas").get().addOnSuccessListener {
            isOn = it.getValue(Boolean::class.java) ?: false
            updateButton()
        }

        binding.btnKipas.setOnClickListener {
            isOn = !isOn
            database.child("ruangBudidaya").child("kipas").setValue(isOn)
            updateButton()
            Toast.makeText(this, if (isOn) "Kipas : ON" else "Kipas : OFF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateButton() {
        binding.btnKipas.background = if (isOn) ResourcesCompat.getDrawable(resources,
            R.drawable.bg_btn_on, null) else ResourcesCompat.getDrawable(resources,
            R.drawable.bg_btn_off, null)
    }
}