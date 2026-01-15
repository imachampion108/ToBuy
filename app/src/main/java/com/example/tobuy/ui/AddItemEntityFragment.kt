package com.example.tobuy.ui

import android.R.attr.progress
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.tobuy.R
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.databinding.FragmentAddItemEntityBinding
import java.util.UUID
import java.util.zip.Inflater
import kotlin.getValue

class AddItemEntityFragment() : BaseFragment() {
    var _binding: FragmentAddItemEntityBinding? = null
    val binding get() = _binding!!
    private val safeArgs : AddItemEntityFragmentArgs by navArgs()
    private val selectedItemEntity : ItemEntity? by lazy {
        sharedViewModel.itemsLiveData.value?.find {
            it.id == safeArgs.selectedItemEntityId
        }
    }
    private var inEditMode : Boolean = false

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

        binding.quantitySeekBar.setOnSeekBarChangeListener(object :
        SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(
                p0: SeekBar?,
                p1: Int,
                p2: Boolean
            ) {
              val currentText = binding.titleEditText.text.toString().trim()
                if (currentText.isEmpty()){
                    return
                }

                val startIndex = currentText.indexOf("[") - 1
                val newText = if (startIndex > 0) {
                    "${currentText.substring(0, startIndex)} [$progress]"
                } else {
                    "$currentText [$progress]"
                }

                val sanitizedText = newText.replace(" [1]", "")
                binding.titleEditText.setText(sanitizedText)
                binding.titleEditText.setSelection(sanitizedText.length)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

        })
        sharedViewModel.transactionCompleteLiveData.observe(viewLifecycleOwner){
            event ->
            event.getContent()?.let{
                if (inEditMode) {
                    navigateUp()
                    return@observe
                }
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

        // set up screen if we are in edit mode
        selectedItemEntity?.let { itemEntity ->
            inEditMode = true
            binding.titleEditText.setText(itemEntity.title)
            binding.descriptionEditText.setText(itemEntity.description)
            when(itemEntity.priority){
                1 -> binding.RadioGroup.check(R.id.radioButtonLow)
                2 -> binding.RadioGroup.check(R.id.radioButtonMedium)
                else -> binding.RadioGroup.check(R.id.radioButtonHigh)
            }

            binding.saveButton.text = "Update"
            mainActivity.supportActionBar?.title = "Update"

            if (itemEntity.title.contains("[")){
                val startIndex = itemEntity.title.indexOf("[") + 1
                val endIndex = itemEntity.title.indexOf("]")
              try{  val progress = itemEntity.title.substring(startIndex,endIndex).toInt()
                binding.quantitySeekBar.progress = progress}
              catch (e: Exception){
                  //whoops
              }
            }
        }
    }

 //   override fun onPause() {
   //     super.onPause()
     //   sharedViewModel.transactionCompleteLiveData.postValue(false)


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

         if (inEditMode){
             val itemEntity = selectedItemEntity!!.copy(
                 title = itemTitle,
                 description = itemDescription,
                 priority = itemPriority
             )
             sharedViewModel.updateItem(itemEntity)
             return
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