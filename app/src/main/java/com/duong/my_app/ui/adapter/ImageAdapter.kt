package com.duong.my_app.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duong.my_app.data.model.Image
import com.duong.my_app.databinding.ItemImageBinding

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private val listImage = mutableListOf<Image>()
    var onSelectItemListener: ((Image, Int) -> Unit)? = null
    var onSelectAllListener: (List<Image>) -> Unit = {}

    fun setData(listImage: List<Image>) {
        listImage.forEach {
            Log.d("listImage", "$it")
        }
        this.listImage.apply {
            clear()
            addAll(listImage)
        }
        notifyDataSetChanged()
    }

    fun selectAll(isSelect: Boolean) {
        listImage.forEach {
            it.isSelected = isSelect
        }
        onSelectAllListener(listImage)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: Image, position: Int) {
            binding.apply {
                with(imgThumb) {
                    Glide.with(context).load(item.path).into(this)
                    clipToOutline = true
                    alpha = if (item.isSelected) 0.5f else 1f
                }
                imgThumb.setOnClickListener {
                    item.isSelected = !item.isSelected
                    onSelectItemListener?.invoke(item, position)
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageAdapter.ViewHolder {
        return ViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listImage.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listImage.getOrNull(position)?.let {
            holder.onBind(it, position)
        }
    }
}