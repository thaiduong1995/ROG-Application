package com.duong.rog.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duong.rog.R
import com.duong.rog.data.model.Folder
import com.duong.rog.databinding.ItemFolderBinding

class FolderAdapter : RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    private val listFolder = mutableListOf<Folder>()
    var onClickItemListener: ((Folder, Int) -> Unit)? = null
    var onAddItemListener: ((Folder, Int) -> Unit)? = null

    fun setData(listFolder: List<Folder>) {
        this.listFolder.apply {
            clear()
            addAll(listFolder)
        }
        notifyDataSetChanged()
    }

    fun insertItem(folder: Folder) {
        listFolder.add(folder)
        notifyItemInserted(listFolder.size)
        notifyItemChanged(listFolder.size)
    }

    fun updateItem(position: Int, size: Int) {
        listFolder.getOrNull(position)?.size = size
        notifyItemChanged(position)
    }

    inner class ViewHolder(
        private val binding: ItemFolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: Folder, position: Int) {
            binding.apply {
                imgThumb.setImageResource(R.drawable.ic_thumb_folder)
                tvName.text = item.name
                tvSize.text = "${item.size}"

                imgAdd.setOnClickListener {
                    onAddItemListener?.invoke(item, position)
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
    ): FolderAdapter.ViewHolder {
        return ViewHolder(
            ItemFolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listFolder.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listFolder.getOrNull(position)?.let {
            holder.onBind(it, position)
        }
    }
}