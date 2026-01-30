package com.example.tobuy.ui.bottomSheet

import androidx.lifecycle.ViewModel
import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.R
import com.example.tobuy.arch.ToBuyViewModel
import com.example.tobuy.databinding.ModelSortOrderItemBinding
import com.example.tobuy.ui.ViewBindingKotlinModel

class BottomSheetEpoxyController(
    private val sortOptions : Array<ToBuyViewModel.HomeViewState.Sort>,
    private val onSelectedSort: (ToBuyViewModel.HomeViewState.Sort) -> Unit
) : EpoxyController() {
    override fun buildModels() {
       sortOptions.forEach {
           SortOrderItemEpoxyModel(it,onSelectedSort).id(it.displayName).addTo(this)
       }
    }
    data class SortOrderItemEpoxyModel(
       val sort : ToBuyViewModel.HomeViewState.Sort,
       val onSelectedSort : (ToBuyViewModel.HomeViewState.Sort) -> Unit
    ) : ViewBindingKotlinModel<ModelSortOrderItemBinding>(R.layout.model_sort_order_item){
        override fun ModelSortOrderItemBinding.bind() {
             textView.text = sort.displayName
              root.setOnClickListener {
                  onSelectedSort(sort)
              }
        }

    }
}