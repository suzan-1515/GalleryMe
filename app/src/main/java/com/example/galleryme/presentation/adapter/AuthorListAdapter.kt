package com.example.galleryme.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.galleryme.databinding.ItemAuthorListBinding

class AuthorListAdapter : RecyclerView.Adapter<AuthorListAdapter.AuthorListViewHolder>() {
    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorListViewHolder {
        val binding =
            ItemAuthorListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AuthorListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuthorListViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class AuthorListViewHolder(private val binding: ItemAuthorListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(differ.currentList[bindingAdapterPosition])
            }
        }

        fun bind(author: String) {
            binding.apply {
                authorTextView.text = author
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)
}