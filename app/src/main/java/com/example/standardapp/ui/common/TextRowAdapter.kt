package com.example.standardapp.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.standardapp.databinding.ItemTextRowBinding

data class TextRow(
    val title: String,
    val body: String,
    val meta: String
)

class TextRowAdapter<T>(
    private val rowMapper: (T) -> TextRow,
    private val onItemClick: (T) -> Unit
) : RecyclerView.Adapter<TextRowAdapter.ViewHolder>() {

    private val items = mutableListOf<T>()

    fun submitItems(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTextRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(rowMapper(item)) {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(
        private val binding: ItemTextRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: TextRow, onClick: () -> Unit) {
            binding.rowTitle.text = row.title
            binding.rowBody.text = row.body
            binding.rowMeta.text = row.meta
            binding.root.setOnClickListener {
                onClick()
            }
        }
    }
}
