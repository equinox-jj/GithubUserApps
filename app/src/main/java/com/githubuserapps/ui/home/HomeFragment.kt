package com.githubuserapps.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.githubuserapps.R
import com.githubuserapps.data.preference.SettingsPreferences
import com.githubuserapps.databinding.FragmentHomeBinding
import com.githubuserapps.ui.ThemeViewModel
import com.githubuserapps.ui.adapter.ItemSearchAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mItemSearchAdapter: ItemSearchAdapter
    private val mHomeViewModel: HomeViewModel by viewModels()
    private val mThemeViewModel: ThemeViewModel by viewModels()

    private lateinit var settingDataStore: SettingsPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingDataStore = SettingsPreferences(requireContext())
        setHasOptionsMenu(true)
        setupAdapter()
        observeViewModel()
        searchUser()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_theme, menu)
        // Dark Mode Menu
        lifecycleScope.launch {
            val isChecked = mThemeViewModel.getThemeMode.first()
            val item = menu.findItem(R.id.dark_mode)
            item.isChecked = isChecked
            setThemeMode(item, isChecked)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dark_mode -> {
                item.isChecked = !item.isChecked
                setThemeMode(item, item.isChecked)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setThemeMode(item: MenuItem, isChecked: Boolean) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            mThemeViewModel.saveToDataStore(true)
            item.setIcon(R.drawable.ic_moon)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            mThemeViewModel.saveToDataStore(false)
            item.setIcon(R.drawable.ic_sun)
        }
    }

    private fun setupAdapter() {
        binding.rvHome.apply {
            mItemSearchAdapter = ItemSearchAdapter()
            binding.rvHome.adapter = mItemSearchAdapter
            binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
            binding.rvHome.setHasFixedSize(true)
        }
    }

    private fun searchUser() {
        binding.svHome.isSubmitButtonEnabled = true
        binding.svHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!!.isNotEmpty()) {
                    mHomeViewModel.searchList(query)
                    binding.svHome.clearFocus()
                } else {
                    Toast.makeText(requireContext(), "Please Input Field.", Toast.LENGTH_SHORT)
                        .show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun observeViewModel() {
        mHomeViewModel.searchUser.observe(viewLifecycleOwner) { searchUser ->
            searchUser.let {
                if (it.isNotEmpty()) {
                    mItemSearchAdapter.newData(it)
                    binding.rvHome.visibility = View.VISIBLE
                    binding.ivNoUserFound.visibility = View.GONE
                    binding.tvNoUserFound.visibility = View.GONE
                    binding.ivSearchUser.visibility = View.GONE
                    binding.tvSearchUser.visibility = View.GONE
                } else {
                    Toast.makeText(requireContext(), "User Not Found.", Toast.LENGTH_SHORT).show()
                    binding.rvHome.visibility = View.GONE
                    binding.ivNoUserFound.visibility = View.VISIBLE
                    binding.tvNoUserFound.visibility = View.VISIBLE
                    binding.ivSearchUser.visibility = View.GONE
                    binding.tvSearchUser.visibility = View.GONE
                }
            }
        }

        mHomeViewModel.isError.observe(viewLifecycleOwner) { isError ->
            isError?.let {
                binding.ivNoSignal.visibility = if (it) View.VISIBLE else View.GONE
                binding.tvNoSignal.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        mHomeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                binding.pbHome.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.ivSearchUser.visibility = View.GONE
                    binding.tvSearchUser.visibility = View.GONE
                    binding.ivNoSignal.visibility = View.GONE
                    binding.tvNoSignal.visibility = View.GONE
                    binding.ivNoUserFound.visibility = View.GONE
                    binding.tvNoUserFound.visibility = View.GONE
                    binding.rvHome.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}