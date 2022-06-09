package com.cs.tvshows.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs.tvshows.data.model.TvShow
import com.cs.tvshows.databinding.ItemTvShowBinding

class TvShowsHomeAdapter(
    private val tvShowsList: MutableList<TvShow> = mutableListOf(),
    private val itemClickListener: ItemClickListener
) : ListAdapter<TvShow, TvShowsHomeAdapter.TvShowViewHolder>(TvShowsComparator()) {

    override fun getItemCount(): Int = tvShowsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        val binding =
            ItemTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvShowViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItem(position: Int): TvShow {
        return tvShowsList[position]
    }

    fun addTvShows(itemsToAdd: List<TvShow>) {
        val size = tvShowsList.size
        tvShowsList.addAll(itemsToAdd)
        notifyItemRangeInserted(size, itemsToAdd.size)
    }

    interface ItemClickListener {
        fun onItemClicked(item: TvShow)
    }

    class TvShowViewHolder(
        private val binding: ItemTvShowBinding,
        private val itemClickListener: ItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TvShow) {
            binding.apply {
                tvShow = item
                itemView.setOnClickListener { itemClickListener.onItemClicked(item) }
                executePendingBindings()
            }
        }
    }

    class TvShowsComparator : DiffUtil.ItemCallback<TvShow>() {
        override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow) =
            oldItem.id == newItem.id
    }
}
