package com.duong.rog.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.duong.rog.data.model.AppData
import com.duong.rog.databinding.ItemAppBinding

class AppAdapter : RecyclerView.Adapter<AppAdapter.ViewHolder>(), Filterable {

    private val listApp = mutableListOf<AppData?>()
    private val listDefault by lazy { mutableListOf<AppData?>() }
    var onClickItemListener: ((AppData, Int) -> Unit)? = null

    fun setData(listApp: List<AppData>) {
        this.listApp.apply {
            clear()
            addAll(listApp)
        }
        this.listDefault.apply {
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
                imgThumb.apply {
                    val icon = context.packageManager.getApplicationIcon(item.packageName)
                    setImageDrawable(icon)
                    clipToOutline = true
                }
                tvName.text = item.appName
                tvSize.text = item.size

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

    override fun getFilter(): Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val listTemp = mutableListOf<AppData?>()
            constraint?.toString()?.let { charString ->
                if (charString.isEmpty()) {
                    listTemp.addAll(listDefault)
                } else {
                    val filteredList = mutableListOf<AppData?>()
                    for (app in listDefault) {
                        if (app?.appName?.lowercase()?.trim()
                                ?.contains(charString.lowercase().trim()) == true
                        ) {
                            filteredList.add(app)
                        }
                    }
                    listTemp.addAll(filteredList)
                }
            }
            val results = FilterResults()
            results.count = listTemp.size
            results.values = listTemp
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            try {
                (results?.values as? List<AppData?>)?.let { list ->
                    listApp.apply {
                        clear()
                        addAll(list)
                    }
                    notifyDataSetChanged()
                }
            } catch (ex: Exception) {

            }
        }
    }
}