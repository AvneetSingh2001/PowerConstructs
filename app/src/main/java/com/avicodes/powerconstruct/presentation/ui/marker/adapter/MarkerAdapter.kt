package com.avicodes.powerconstruct.presentation.ui.marker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.powerconstruct.data.models.Marker
import com.avicodes.powerconstruct.data.utils.TimeCalc
import com.avicodes.powerconstruct.databinding.ItemMarkerBinding

class MarkerAdapter: RecyclerView.Adapter<MarkerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemMarkerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val data = differ.currentList[position]
                data?.let { marker ->
                    tvTitle.text = marker.title
                    tvDesc.text = marker.description
                    tvTime.text = TimeCalc.getTimeAgo(marker.timeCreated.toLong())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMarkerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    private var callback = object : DiffUtil.ItemCallback<Marker>() {
        override fun areItemsTheSame(oldItem: Marker, newItem: Marker): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Marker, newItem: Marker): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)
}