package com.example.tobuy.ui

import android.R.attr.category
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleObserver
import com.example.tobuy.database.entity.CategoryEntity
import com.example.tobuy.databinding.AddCategoryFragmentBinding
import java.util.UUID

class AddCategoryFragment : BaseFragment() {
    private var _binding : AddCategoryFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddCategoryFragmentBinding.inflate(inflater,container,false)
         return binding.root
    }

    override fun onResume() {
        super.onResume()
            mainActivity.hideKeyboard(requireView())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.categoryNameEditText.requestFocus()
        mainActivity.showKeyboard()
        binding.saveButton.setOnClickListener {
             saveCategoryToDatabase()
        }

        sharedViewModel.transactionCompleteLiveData.observe(viewLifecycleOwner){ completed ->
            if (completed){
                navigateUp()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sharedViewModel.transactionCompleteLiveData.postValue(false )
    }
    private fun saveCategoryToDatabase(){
        val CategoryName = binding.categoryNameEditText.text.toString().trim()
        if (CategoryName.isEmpty()){
            binding.categoryNameTextField.error = "* Required Field"
            return
        }
        val categoryEntity = CategoryEntity(
            id = UUID.randomUUID().toString(),
            name = CategoryName
        )
        sharedViewModel.insertCategory(categoryEntity)
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}