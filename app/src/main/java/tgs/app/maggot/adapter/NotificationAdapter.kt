package tgs.app.maggot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tgs.app.maggot.database.NotificationEntity
import tgs.app.maggot.R
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private val notifications = mutableListOf<NotificationEntity>()

    fun submitList(notifications: List<NotificationEntity>) {
        this.notifications.clear()
        this.notifications.addAll(notifications)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notifikasi, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount(): Int = notifications.size

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        fun bind(notification: NotificationEntity) {
            tvMessage.text = notification.message
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            tvDate.text = dateFormat.format(notification.receivedAt)
        }
    }
}
