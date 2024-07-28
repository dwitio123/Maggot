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
import tgs.app.maggot.databinding.ActivityAboutBinding
import tgs.app.maggot.databinding.ActivityPengusirHamaBinding

class PengusirHamaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPengusirHamaBinding
    private lateinit var database: DatabaseReference
    private var isOn: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPengusirHamaBinding.inflate(layoutInflater)
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

        database.child("pengusirHama").child("pengusirHama").get().addOnSuccessListener {
            isOn = it.getValue(Boolean::class.java) ?: false
            updateButton()
        }

        database.child("pengusirHama").child("status").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val status = snapshot.getValue(String::class.java)
                binding.tvStatus.text = status
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PengusirHamaActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnPengusirHama.setOnClickListener {
            isOn = !isOn
            database.child("pengusirHama").child("pengusirHama").setValue(isOn)
            updateButton()
            Toast.makeText(this, if (isOn) "Pengusir Hama : ON" else "Pengusir Hama : OFF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateButton() {
        binding.btnPengusirHama.background = if (isOn) ResourcesCompat.getDrawable(resources,
            R.drawable.bg_btn_on, null) else ResourcesCompat.getDrawable(resources,
            R.drawable.bg_btn_off, null)
    }
}