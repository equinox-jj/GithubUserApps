package com.githubuserapps.ui.follower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.githubuserapps.databinding.FragmentFollowerBinding
import com.githubuserapps.ui.adapter.ItemFollowAdapter

class FollowerFragment : Fragment() {

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    private lateinit var mFollowerAdapter: ItemFollowAdapter
    private val mFollowerViewModel: FollowerViewModel by viewModels()
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
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
        binding.rvFollowers.apply {
            mFollowerAdapter = ItemFollowAdapter()
            binding.rvFollowers.adapter = mFollowerAdapter
            binding.rvFollowers.setHasFixedSize(true)
            binding.rvFollowers.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        mFollowerViewModel.followerUserList(username)
        mFollowerViewModel.followerUser.observe(viewLifecycleOwner) { followerUser ->
            followerUser.let {
                if (it.isNotEmpty()) {
                    mFollowerAdapter.newData(it)
                    binding.rvFollowers.visibility = View.VISIBLE
                    binding.ivNoFollowerFound.visibility = View.GONE
                    binding.tvNoFollowerFound.visibility = View.GONE
                } else {
                    binding.rvFollowers.visibility = View.GONE
                    binding.ivNoFollowerFound.visibility = View.VISIBLE
                    binding.tvNoFollowerFound.visibility = View.VISIBLE
                }
            }
        }

        mFollowerViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                binding.pbFollower.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.rvFollowers.visibility = View.GONE
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