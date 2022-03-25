package com.githubuserapps.ui.detail

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.githubuserapps.R
import com.githubuserapps.databinding.FragmentDetailBinding
import com.githubuserapps.ui.adapter.ViewPagerAdapter
import com.githubuserapps.ui.follower.FollowerFragment
import com.githubuserapps.ui.following.FollowingFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val mDetailViewModel: DetailViewModel by viewModels()
    private val args by navArgs<DetailFragmentArgs>()

    // FAVORITE USER SETUP
    private var userSaved = false
    private var savedUserId = 0
    private lateinit var favMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        observeViewModel()
        setupViewPager()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        favMenuItem = menu.findItem(R.id.favorite_menu)
        checkSavedFavoriteMovies(favMenuItem)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.favorite_menu && !userSaved) {
            insertFavoriteUser(item)
        } else if (item.itemId == R.id.favorite_menu && userSaved) {
            removeFavoriteUser(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeViewModel() {
        mDetailViewModel.detailUser(args.username)
        mDetailViewModel.detailUser.observe(viewLifecycleOwner) { user ->
            user.let {
                binding.tvUsrnmDetailUsr.text = it.login
                binding.tvNameDetailUsr.text = it.name
                if (it.company?.isNotEmpty() == true) {
                    binding.tvCompanyDtl.text = it.company
                } else binding.tvCompanyDtl.text = getText(R.string.no_data)
                if (it.location?.isNotEmpty() == true) {
                    binding.tvLocationDtl.text = it.location
                } else binding.tvLocationDtl.text = getText(R.string.no_data)
                binding.tvFollowerDtl.text = it.followers.toString()
                binding.tvFollowingDtl.text = it.following.toString()
                binding.tvRepositoryDtl.text = it.publicRepos.toString()
                binding.ivDetailUsr.load(it.avatarUrl) {
                    transformations(CircleCropTransformation())
                    crossfade(300)
                }
            }
        }

        mDetailViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                binding.pbDetail.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.tvUsrnmDetailUsr.visibility = View.GONE
                    binding.tvNameDetailUsr.visibility = View.GONE
                    binding.tvCompanyDtl.visibility = View.GONE
                    binding.tvLocationDtl.visibility = View.GONE
                    binding.tvFollowerDtl.visibility = View.GONE
                    binding.tvFollowingDtl.visibility = View.GONE
                    binding.tvRepositoryDtl.visibility = View.GONE
                    binding.ivDetailUsr.visibility = View.GONE
                } else {
                    binding.tvUsrnmDetailUsr.visibility = View.VISIBLE
                    binding.tvNameDetailUsr.visibility = View.VISIBLE
                    binding.tvCompanyDtl.visibility = View.VISIBLE
                    binding.tvLocationDtl.visibility = View.VISIBLE
                    binding.tvFollowerDtl.visibility = View.VISIBLE
                    binding.tvFollowingDtl.visibility = View.VISIBLE
                    binding.tvRepositoryDtl.visibility = View.VISIBLE
                    binding.ivDetailUsr.visibility = View.VISIBLE
                }
            }
        }

        mDetailViewModel.isError.observe(viewLifecycleOwner) { isError ->
            isError?.let { }
        }
    }

    private fun setupViewPager() {
        val fragments = ArrayList<Fragment>()
        fragments.add(FollowerFragment())
        fragments.add(FollowingFragment())

        val titles = ArrayList<String>()
        titles.add("Follower")
        titles.add("Following")

        val bundle = Bundle()
        bundle.putString("userBundle", args.username)

        val viewPagerAdapter = ViewPagerAdapter(
            bundle,
            fragments,
            requireActivity()
        )

        binding.vpDetail.apply { adapter = viewPagerAdapter }

        TabLayoutMediator(binding.tlDetailUsr, binding.vpDetail) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    // Favorite User
    private fun checkSavedFavoriteMovies(favMenuItem: MenuItem) {
        mDetailViewModel.getFavoriteUser().observe(viewLifecycleOwner) { favoriteMoviesEntity ->
            try {
                for (savedUsers in favoriteMoviesEntity) {
                    if (savedUsers.id == args.userId) {
                        changeFavoriteMenuItemColor(favMenuItem, R.color.red)
                        savedUserId = savedUsers.id
                        userSaved = true
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun insertFavoriteUser(item: MenuItem) {
        mDetailViewModel.insertUserToFavorite(
            args.userId,
            args.username,
            args.avatarUrl,
            args.htmlUrl
        )
        changeFavoriteMenuItemColor(item, R.color.red)
        showSnackBarFavorite("User Saved.")
        userSaved = true
    }

    private fun removeFavoriteUser(item: MenuItem) {
        mDetailViewModel.deleteFavoriteUser(
            args.userId,
            args.username,
            args.avatarUrl,
            args.htmlUrl
        )
        changeFavoriteMenuItemColor(item, R.color.grey)
        showSnackBarFavorite("Removed From Favorites.")
        userSaved = false
    }

    private fun showSnackBarFavorite(message: String) {
        Snackbar.make(
            binding.userDetailLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    private fun changeFavoriteMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(requireContext(), color))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        changeFavoriteMenuItemColor(favMenuItem, R.color.white)
    }

}