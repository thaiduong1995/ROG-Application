package com.duong.rog.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duong.rog.data.model.Image
import com.duong.rog.databinding.ItemImagePreviewBinding

class ImagePreviewAdapter : RecyclerView.Adapter<ImagePreviewAdapter.ViewHolder>() {

    private val imageList = mutableListOf<Image>()
    var onClickItemListener: ((Image, Int) -> Unit)? = null

    fun setData(listImage: List<Image>) {
        this.imageList.apply {
            clear()
            addAll(listImage)
        }
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        imageList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, imageList.size)
    }

    inner class ViewHolder(
        private val binding: ItemImagePreviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: Image, position: Int) {
            binding.apply {
                with(photoView) {
                    Glide.with(context).load(item.path).into(this)
                    clipToOutline = true
                    alpha = if (item.isSelected) 0.5f else 1f

                    setOnClickListener {
                        onClickItemListener?.invoke(item, position)
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagePreviewAdapter.ViewHolder {
        return ViewHolder(
            ItemImagePreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        imageList.getOrNull(position)?.let {
            holder.onBind(it, position)
        }
    }
}