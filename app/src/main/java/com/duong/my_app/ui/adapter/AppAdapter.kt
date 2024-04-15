package com.duong.my_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duong.my_app.data.model.AppData
import com.duong.my_app.databinding.ItemAppBinding

class AppAdapter : RecyclerView.Adapter<AppAdapter.ViewHolder>() {

    private val listApp = mutableListOf<AppData>()
    var onClickItemListener: ((AppData, Int) -> Unit)? = null

    fun setData(listApp: List<AppData>) {
        this.listApp.apply {
            clear()
            addAll(listApp)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemAppBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: AppData, position: Int) {
            binding.apply {
                with(imgThumb) {
                    Glide.with(context)
                        .load("pkg:${item.packageName}")
                        .into(this)
                    clipToOutline = true
                }

                root.setOnClickListener {
                    onClickItemListener?.invoke(item, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppAdapter.ViewHolder {
        return ViewHolder(
            ItemAppBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listApp.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listApp.getOrNull(position)?.let {
            holder.onBind(it, position)
        }
    }
}