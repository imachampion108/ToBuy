package com.example.tobuy.ui

import android.content.res.ColorStateList
import android.graphics.Typeface
import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.R
import com.example.tobuy.arch.ToBuyViewModel
import com.example.tobuy.databinding.ModelCategoryItemSelectionBinding

class CategoriesViewStateEpoxyController(
    private val onCategorySelected: (String) -> Unit
) : EpoxyController() {

    var viewState = ToBuyViewModel.CategoriesViewState()
        set(value){
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        if(viewState.isLoading){

            LoadingEpoxyModel().id("loading").addTo(this)
            return
        }

        viewState.itemsList.forEach { item ->
            CategoriesViewStateItem(item, onCategorySelected).id(item.categoryEntity.id).addTo(this)

        }
    }

    data class CategoriesViewStateItem(
        val item : ToBuyViewModel.CategoriesViewState.item,
        private val onCategorySelected :(String) -> Unit
    ) : ViewBindingKotlinModel<ModelCategoryItemSelectionBinding>(R.layout.model_category_item_selection){
        override fun ModelCategoryItemSelectionBinding.bind() {
            
            textView.text = item.categoryEntity.name
            root.setOnClickListener{
                onCategorySelected(item.categoryEntity.id)
            }

            val colorRes = if(item.isSelected) com.google.android.material.R.attr.colorSecondary else androidx.appcompat.R.attr.colorPrimary
            val color = root.getAttrColor(colorRes)
            textView.setTextColor(color)
            textView.typeface = if (item.isSelected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            root.setStrokeColor(ColorStateList.valueOf(color))
        }


    }
}