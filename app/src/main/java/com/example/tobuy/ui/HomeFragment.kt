package com.example.tobuy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyTouchHelper
import com.example.tobuy.R
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.databinding.FragmentHomeBinding

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

            EpoxyTouchHelper.initSwiping(binding.epoxyRecyclerView)
                  .right()
                  .withTarget(HomeEpoxyController.ItemEntityEpoxyModel::class.java)
                  .andCallbacks(object : EpoxyTouchHelper.SwipeCallbacks<HomeEpoxyController.ItemEntityEpoxyModel>(){
                        override fun onSwipeCompleted(
                              model: HomeEpoxyController.ItemEntityEpoxyModel?,
                              itemview: View?,
                              poition: Int,
                              direction: Int
                        ){
                              val itemThatWasRemoved = model?.itemEntity ?: return
                              sharedViewModel.deleteItem(itemThatWasRemoved)
                        }})
                  }


      override fun onResume() {
            super.onResume()
            mainActivity.hideKeyboard(requireView())
      }
  //    override fun onDeleteItemEntity(itemEntity: ItemEntity) {
    //      sharedViewModel.deleteItem(itemEntity)
    //      }

      override fun onBumpPriority(itemEntity: ItemEntity) {
              val currrentPriority = itemEntity.priority
              var newPriority = currrentPriority + 1
             if (newPriority > 3){
                   newPriority = 1
             }
            val updatedPriority = itemEntity.copy(priority = newPriority)
            sharedViewModel.updateItem(updatedPriority)
      }

      override fun OnSelectedItem(itemEntity: ItemEntity) {
            val navDirection = HomeFragmentDirections.homeFragmentToAddItemEntity(itemEntity.id)
            navigateViaGraph(navDirection)
      }


      override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
      }
}