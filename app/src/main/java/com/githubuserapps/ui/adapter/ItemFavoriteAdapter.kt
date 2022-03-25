package com.githubuserapps.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.githubuserapps.data.model.SearchItems
import com.githubuserapps.databinding.ItemSearchUserBinding
import com.githubuserapps.ui.favorite.FavoriteFragmentDirections
import com.githubuserapps.util.DiffUtils

class ItemFavoriteAdapter : RecyclerView.Adapter<ItemFavoriteAdapter.FavoriteViewHolder>() {

    private var favoriteUserItem = ArrayList<SearchItems>()

    class FavoriteViewHolder(var binding: ItemSearchUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun bind(parent: ViewGroup): FavoriteViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSearchUserBinding.inflate(layoutInflater, parent, false)
                return FavoriteViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder.bind(parent)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val bindSearchData = favoriteUserItem[position]
        holder.binding.tvUsrnmItmList.text = bindSearchData.login
        holder.binding.tvGithubUrlItmList.text = bindSearchData.htmlUrl
        holder.binding.ivAvatarItmList.load(bindSearchData.avatarUrl) {
            crossfade(300)
        }

        holder.itemView.setOnClickListener {
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
                bindSearchData.login!!,
                bindSearchData.id,
                bindSearchData.avatarUrl!!,
                bindSearchData.htmlUrl!!
            )
            holder.itemView.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int = favoriteUserItem.size

    fun newData(newData: List<SearchItems>) {
        val diffUtil = DiffUtils(favoriteUserItem, newData)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        favoriteUserItem = newData as ArrayList<SearchItems>
        diffUtilResult.dispatchUpdatesTo(this)
    }

}