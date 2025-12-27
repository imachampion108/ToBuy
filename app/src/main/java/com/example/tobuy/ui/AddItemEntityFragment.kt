package com.example.tobuy.ui

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tobuy.R
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.databinding.FragmentAddItemEntityBinding
import java.util.UUID
import java.util.zip.Inflater

class AddItemEntityFragment() : BaseFragment() {
    var _binding: FragmentAddItemEntityBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemEntityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            saveItemEntityToDatabase()
        }
        sharedViewModel.transactionCompleteLiveData.observe(viewLifecycleOwner){
            complete ->
            if (complete){
                Toast.makeText(requireActivity(),"Item Saved!", Toast.LENGTH_SHORT).show()
            binding.titleEditText.text = null
                binding.titleTextField.requestFocus()
                mainActivity.showKeyboard()
            binding.descriptionEditText.text = null
            binding.RadioGroup.check(R.id.radioButtonLow)
        }
    }
        mainActivity.showKeyboard()
        binding.titleTextField.requestFocus()
    }

    override fun onPause() {
        super.onPause()
        sharedViewModel.transactionCompleteLiveData.postValue(false)
    }

     private fun saveItemEntityToDatabase() {
        val itemTitle = binding.titleEditText.text.toString().trim()
        if (itemTitle.isEmpty()) {
            binding.titleTextField.error = "Required Text"
            return
        } else
            binding.titleTextField.error = null

        val itemDescription = binding.descriptionEditText.text.toString().trim()
        val itemPriority = when(binding.RadioGroup.checkedRadioButtonId){
            R.id.radioButtonLow -> 1
            R.id.radioButtonMedium -> 2
            R.id.radioButtonHigh -> 3
            else -> 0
        }
        val itemEntity = ItemEntity(
            id = UUID.randomUUID().toString(),
            title = itemTitle,
            description = itemDescription,
            priority = itemPriority,
            createdAt = System.currentTimeMillis(),
            categoryId = "yet to create"

        )
        sharedViewModel.insertItem(itemEntity)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}