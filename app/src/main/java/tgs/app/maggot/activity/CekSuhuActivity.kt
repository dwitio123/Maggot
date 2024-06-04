package tgs.app.maggot.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tgs.app.maggot.R
import tgs.app.maggot.databinding.ActivityCekSuhuBinding

class CekSuhuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCekSuhuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCekSuhuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.ruangBudidaya.setOnClickListener {
            startActivity(Intent(this, RuangBudidayaActivity::class.java))
        }

        binding.boxMaggot.setOnClickListener {
            startActivity(Intent(this, BoxMaggotActivity::class.java))
        }
    }
}