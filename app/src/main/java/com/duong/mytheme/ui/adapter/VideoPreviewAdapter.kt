package com.duong.mytheme.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duong.mytheme.R
import com.duong.mytheme.data.model.Video
import com.duong.mytheme.databinding.ItemVideoPreviewBinding

class VideoPreviewAdapter : RecyclerView.Adapter<VideoPreviewAdapter.ViewHolder>() {

    private val listVideo = mutableListOf<Video>()
    var onClickItemListener: (Video) -> Unit = {}

    fun setData(listVideo: List<Video>) {
        this.listVideo.apply {
            clear()
            addAll(listVideo)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemVideoPreviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: Video, position: Int) {
            binding.apply {
                with(imgThumb) {
                    Glide.with(context).load(item.path).error(R.drawable.logo).into(this)
                    clipToOutline = true
                }

                root.setOnClickListener {
                    onClickItemListener(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVideoPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listVideo.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listVideo.getOrNull(position)?.let {
            holder.onBind(it, position)
        }
    }
}