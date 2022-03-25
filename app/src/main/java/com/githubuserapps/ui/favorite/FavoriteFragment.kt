package com.githubuserapps.ui.favorite

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.githubuserapps.R
import com.githubuserapps.data.local.entity.UserEntity
import com.githubuserapps.data.model.SearchItems
import com.githubuserapps.databinding.FragmentFavoriteBinding
import com.githubuserapps.ui.adapter.ItemFavoriteAdapter
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var mFavoriteAdapter: ItemFavoriteAdapter
    private val mFavoriteViewModel: FavoriteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupAdapter()
        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_favorite, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_user -> {
                mFavoriteViewModel.deleteAllFavoriteUser()
                showSnackBar()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupAdapter() {
        binding.rvFavorite.apply {
            mFavoriteAdapter = ItemFavoriteAdapter()
            binding.rvFavorite.adapter = mFavoriteAdapter
            binding.rvFavorite.layoutManager = LinearLayoutManager(requireContext())
            binding.rvFavorite.setHasFixedSize(true)
        }
    }

    private fun showSnackBar() {
        Snackbar.make(
            binding.root,
            "All User Removed.",
            Snackbar.LENGTH_SHORT
        ).setAction("Okay.") {}
            .show()
    }

    private fun observeViewModel() {
        mFavoriteViewModel.getFavoriteUser().observe(viewLifecycleOwner) { userEntity ->
            if (userEntity.isNotEmpty()) {
                binding.rvFavorite.visibility = View.VISIBLE
                binding.ivNoFavorite.visibility = View.GONE
                binding.tvNoFavorite.visibility = View.GONE
                val list = mapList(userEntity)
                mFavoriteAdapter.newData(list)
            } else {
                binding.rvFavorite.visibility = View.GONE
                binding.ivNoFavorite.visibility = View.VISIBLE
                binding.tvNoFavorite.visibility = View.VISIBLE
            }
        }
    }

    private fun mapList(userEntity: List<UserEntity>): ArrayList<SearchItems> {
        val listUser = ArrayList<SearchItems>()
        for (user in userEntity) {
            val userMapped = SearchItems(
                user.username,
                user.avatar_url,
                user.id,
                user.html_url
            )
            listUser.add(userMapped)
        }
        return listUser
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}