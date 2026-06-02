package com.example.standardapp.ui.common

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
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
    private val onItemClick: (T) -> Unit,
    private val highlightColor: Int = Color.YELLOW
) : RecyclerView.Adapter<TextRowAdapter.ViewHolder>() {

    private val items = mutableListOf<T>()
    private var highlightQuery: String = ""

    fun submitItems(
        newItems: List<T>,
        highlightQuery: String = ""
    ) {
        this.highlightQuery = highlightQuery.trim()
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
        holder.bind(
            row = rowMapper(item),
            highlightQuery = highlightQuery,
            highlightColor = highlightColor
        ) {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(
        private val binding: ItemTextRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            row: TextRow,
            highlightQuery: String,
            highlightColor: Int,
            onClick: () -> Unit
        ) {
            binding.rowTitle.text = row.title.highlightMatches(highlightQuery, highlightColor)
            binding.rowBody.text = row.body.highlightMatches(highlightQuery, highlightColor)
            binding.rowMeta.text = row.meta.highlightMatches(highlightQuery, highlightColor)
            binding.root.setOnClickListener {
                onClick()
            }
        }
    }
}

private fun String.highlightMatches(
    query: String,
    highlightColor: Int
): CharSequence {
    if (query.isBlank()) return this

    val highlightedText = SpannableString(this)
    var matchStart = indexOf(query, startIndex = 0, ignoreCase = true)

    while (matchStart >= 0) {
        val matchEnd = matchStart + query.length
        highlightedText.setSpan(
            BackgroundColorSpan(highlightColor),
            matchStart,
            matchEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        matchStart = indexOf(query, startIndex = matchEnd, ignoreCase = true)
    }

    return highlightedText
}
