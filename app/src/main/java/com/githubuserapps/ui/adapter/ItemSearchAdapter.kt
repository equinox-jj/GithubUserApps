package com.githubuserapps.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.githubuserapps.data.model.SearchItems
import com.githubuserapps.databinding.ItemSearchUserBinding
import com.githubuserapps.ui.home.HomeFragmentDirections
import com.githubuserapps.util.DiffUtils

class ItemSearchAdapter : RecyclerView.Adapter<ItemSearchAdapter.SearchViewHolder>() {

    private var searchUserItem = ArrayList<SearchItems>()

    class SearchViewHolder(var binding: ItemSearchUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun bind(parent: ViewGroup): SearchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSearchUserBinding.inflate(layoutInflater, parent, false)
                return SearchViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.bind(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val bindSearchData = searchUserItem[position]
        holder.binding.tvUsrnmItmList.text = bindSearchData.login
        holder.binding.tvGithubUrlItmList.text = bindSearchData.htmlUrl
        holder.binding.ivAvatarItmList.load(bindSearchData.avatarUrl) {
            crossfade(300)
        }

        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                bindSearchData.login!!,
                bindSearchData.id,
                bindSearchData.avatarUrl!!,
                bindSearchData.htmlUrl!!
            )
            holder.itemView.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int = searchUserItem.size

    fun newData(newData: List<SearchItems>) {
        val diffUtil = DiffUtils(searchUserItem, newData)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        searchUserItem = newData as ArrayList<SearchItems>
        diffUtilResult.dispatchUpdatesTo(this)
    }

}
