package com.githubuserapps.ui.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.githubuserapps.databinding.FragmentFollowingBinding
import com.githubuserapps.ui.adapter.ItemFollowAdapter

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private lateinit var mFollowingAdapter: ItemFollowAdapter
    private val mFollowingViewModel: FollowingViewModel by viewModels()
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        // Follower Args
        val args = arguments
        username = args?.getString("userBundle").toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        observeViewModel()
    }

    private fun setupRecycler() {
        binding.rvFollowing.apply {
            mFollowingAdapter = ItemFollowAdapter()
            binding.rvFollowing.adapter = mFollowingAdapter
            binding.rvFollowing.setHasFixedSize(true)
            binding.rvFollowing.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        mFollowingViewModel.followingUserList(username)
        mFollowingViewModel.followingUser.observe(viewLifecycleOwner) { followerUser ->
            followerUser.let {
                if (it.isNotEmpty()) {
                    mFollowingAdapter.newData(it)
                    binding.rvFollowing.visibility = View.VISIBLE
                    binding.ivNoFollowerFound.visibility = View.GONE
                    binding.tvNoFollowerFound.visibility = View.GONE
                } else {
                    binding.rvFollowing.visibility = View.GONE
                    binding.ivNoFollowerFound.visibility = View.VISIBLE
                    binding.tvNoFollowerFound.visibility = View.VISIBLE
                }
            }
        }

        mFollowingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                binding.pbFollowing.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.rvFollowing.visibility = View.GONE
                    binding.ivNoFollowerFound.visibility = View.GONE
                    binding.tvNoFollowerFound.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}