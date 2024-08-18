package tgs.app.maggot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tgs.app.maggot.R
import tgs.app.maggot.model.RiwayatData

class RiwayatAdapter(private val riwayatList: List<RiwayatData>) : RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val riwayatData = riwayatList[position]
        holder.bind(riwayatData)
    }

    override fun getItemCount(): Int {
        return riwayatList.size
    }

    class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
        private val tvSuhu: TextView = itemView.findViewById(R.id.tvSuhu)
        private val tvKelembaban: TextView = itemView.findViewById(R.id.tvKelembaban)

        fun bind(riwayatData: RiwayatData) {
            tvTimestamp.text = riwayatData.timestamp
            tvSuhu.text = "Suhu: ${riwayatData.suhu}"
            tvKelembaban.text = "Kelembaban: ${riwayatData.kelembaban}"
        }
    }
}