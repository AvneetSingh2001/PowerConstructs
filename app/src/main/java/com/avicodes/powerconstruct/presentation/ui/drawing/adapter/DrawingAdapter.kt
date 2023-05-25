package com.avicodes.powerconstruct.presentation.ui.drawing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.powerconstruct.data.models.Drawing
import com.avicodes.powerconstruct.data.utils.TimeCalc.getTimeAgo
import com.avicodes.powerconstruct.databinding.ItemDrawingBinding
import com.bumptech.glide.Glide

class DrawingAdapter : RecyclerView.Adapter<DrawingAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemDrawingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val data = differ.currentList[position]
                data?.let { drawing ->
                    Glide.with(ivDrawing.context)
                        .load(drawing.url)
                        .into(ivDrawing)

                    tvDrawingName.text = drawing.name

                    tvMarkerCount.text = drawing.markerCount.toString()

                    tvTime.text = getTimeAgo(drawing.timeAdded.toLong())

                    root.setOnClickListener {
                        onItemClickListener?.let {
                            it(data)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDrawingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    private var callback = object : DiffUtil.ItemCallback<Drawing>() {
        override fun areItemsTheSame(oldItem: Drawing, newItem: Drawing): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Drawing, newItem: Drawing): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    private var onItemClickListener: ((Drawing) -> Unit)? = null

    fun setOnItemClickListener(listener: (Drawing) -> Unit) {
        onItemClickListener = listener
    }
}