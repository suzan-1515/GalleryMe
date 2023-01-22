package com.example.galleryme.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.util.CoilUtils
import com.example.galleryme.R
import com.example.galleryme.databinding.ItemImageListBinding
import com.example.galleryme.domain.model.Image

class ImageListAdapter : RecyclerView.Adapter<ImageListAdapter.ImageListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListViewHolder {
        val binding =
            ItemImageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageListViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.alpha)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class ImageListViewHolder(private val binding: ItemImageListBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(image: Image) {
            binding.apply {
                CoilUtils.apply {
                    imageView.load(image.downloadURL) {
                        crossfade(true)
                        placeholder(R.drawable.placeholder)
                    }
                }
                authorTextView.text =
                    binding.root.context.getString(R.string.author_name, image.author)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)
}