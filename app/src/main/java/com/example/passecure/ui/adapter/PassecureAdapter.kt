package com.example.passecure.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.passecure.R
import com.example.passecure.data.model.PassecureItem
import com.example.passecure.databinding.PassItemBinding

class PassecureAdapter(private val onTap: (passecureItem: PassecureItem) -> Unit): RecyclerView.Adapter<PassecureAdapter.PassItem>() {
    private var data: List<PassecureItem> = listOf()

    class PassItem(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = PassItemBinding.bind(itemView)

        fun configure(passecureItem: PassecureItem, position: Int) {
            binding.numberTV.text = "#$position"
            binding.nameTV.text = passecureItem.name
        }
    }

    fun updateData(passData: List<PassecureItem>) {
        data = passData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassItem {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.pass_item, parent, false)
        return PassItem(itemView)
    }

    override fun onBindViewHolder(holder: PassItem, position: Int) {
        val pass = data[position]
        holder.configure(pass, position+1)
        holder.itemView.setOnClickListener {
            onTap(pass)
        }
    }
}












