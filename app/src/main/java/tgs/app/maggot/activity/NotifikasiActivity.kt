package tgs.app.maggot.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import tgs.app.maggot.database.DatabaseProvider
import tgs.app.maggot.adapter.NotificationAdapter
import tgs.app.maggot.R
import tgs.app.maggot.databinding.ActivityNotifikasiBinding

class NotifikasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotifikasiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        val database = DatabaseProvider.getDatabase(applicationContext)
        val notificationDao = database.notificationDao()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = NotificationAdapter()
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            val notifications = notificationDao.getAllNotifications()
            adapter.submitList(notifications)
        }
    }
}