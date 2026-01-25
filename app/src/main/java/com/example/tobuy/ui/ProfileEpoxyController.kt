package com.example.tobuy.ui

import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.R
import com.example.tobuy.ui.addHeaderModel
import com.example.tobuy.ui.HomeEpoxyController
import com.example.tobuy.database.entity.CategoryEntity
import com.example.tobuy.databinding.ModelCategoryBinding
import com.example.tobuy.databinding.ModelEmptyButtonBinding

class ProfileEpoxyController(private val onCategoryEmptyStateClicked : () -> Unit
) : EpoxyController() {

    var categories : List<CategoryEntity> = emptyList()
        set(value){
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        // categories section
        addHeaderModel("Categories")
        categories.forEach {
            CategoryEpoxyModel(it).id(it.id).addTo(this)
        }

        EmptyButtonEpoxyModel("Add Category",onCategoryEmptyStateClicked)
            .id("add_category")
            .addTo(this)
    }

    data class CategoryEpoxyModel(
        val categoryEntity: CategoryEntity
    ): ViewBindingKotlinModel<ModelCategoryBinding>(R.layout.model_category){
        override fun ModelCategoryBinding.bind() {
            textView.text = categoryEntity.name
        }
    }

    data class EmptyButtonEpoxyModel(
        val buttonText : String,
        val onClicked : () -> Unit
    ) : ViewBindingKotlinModel<ModelEmptyButtonBinding>(R.layout.model_empty_button){
        override fun ModelEmptyButtonBinding.bind() {
            button.text = buttonText
            button.setOnClickListener { onClicked.invoke() }
        }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}}