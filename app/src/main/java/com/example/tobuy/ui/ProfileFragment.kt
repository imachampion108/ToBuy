package com.example.tobuy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tobuy.R
import com.example.tobuy.database.entity.CategoryEntity
import com.example.tobuy.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val ProfileEpoxyController = ProfileEpoxyController(
        onCategoryEmptyStateClicked = ::onCategoryEmptyStateClicked
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.epoxyRecyclerView.setController(ProfileEpoxyController)
        sharedViewModel.categoryLiveData.observe(viewLifecycleOwner){ categoryEntities ->
            ProfileEpoxyController.categories = categoryEntities
        }
    }

    private fun onCategoryEmptyStateClicked(){
        navigateViaGraph(R.id.profileFragment_to_addCategoryFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}