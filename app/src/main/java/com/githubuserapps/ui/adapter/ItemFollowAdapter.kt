package com.githubuserapps.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.githubuserapps.data.model.SearchItems
import com.githubuserapps.databinding.ItemSearchUserBinding
import com.githubuserapps.util.DiffUtils

class ItemFollowAdapter : RecyclerView.Adapter<ItemFollowAdapter.FollowViewHolder>() {

    private var followUserItem = ArrayList<SearchItems>()

    class FollowViewHolder(var binding: ItemSearchUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun bind(parent: ViewGroup): FollowViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSearchUserBinding.inflate(layoutInflater, parent, false)
                return FollowViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        return FollowViewHolder.bind(parent)
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        val bindSearchData = followUserItem[position]
        holder.binding.tvUsrnmItmList.text = bindSearchData.login
        holder.binding.tvGithubUrlItmList.text = bindSearchData.htmlUrl
        holder.binding.ivAvatarItmList.load(bindSearchData.avatarUrl) {
            crossfade(300)
        }
    }

    override fun getItemCount(): Int = followUserItem.size

    fun newData(newData: List<SearchItems>) {
        val diffUtil = DiffUtils(followUserItem, newData)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        followUserItem = newData as ArrayList<SearchItems>
        diffUtilResult.dispatchUpdatesTo(this)
    }
}