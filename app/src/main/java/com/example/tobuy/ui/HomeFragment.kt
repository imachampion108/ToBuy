package com.example.tobuy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tobuy.R
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.databinding.FragmentHomeBinding
import java.util.zip.Inflater

class HomeFragment : BaseFragment(), itemEntityInterface {
      var _binding: FragmentHomeBinding? = null
      val binding get() = _binding!!

      override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
      ): View? {
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
            return binding.root
      }

      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.fab.setOnClickListener {
                  navigateViaGraph(R.id.homeFragment_to_AddItemEntity)
            }
            val controller = HomeEpoxyController(this)
            binding.epoxyRecyclerView.setController(controller)
            sharedViewModel.itemsLiveData.observe(viewLifecycleOwner) { itemEntities ->
                  controller.itemEntityList = itemEntities as ArrayList<ItemEntity>
            }
      }


      override fun onDeleteItemEntity(itemEntity: ItemEntity) {
            TODO("Not yet implemented")
      }

      override fun onBumpPriority(itemEntity: ItemEntity) {
            TODO("Not yet implemented")
      }

      override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
      }
}