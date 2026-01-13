package com.example.tobuy.ui

import com.example.tobuy.R
import com.example.tobuy.databinding.ModelHeaderItemBinding
import com.example.tobuy.ui.ViewBindingKotlinModel



data class HeaderEpoxyModel(
    val headerText: String
) : ViewBindingKotlinModel<ModelHeaderItemBinding>(R.layout.model_header_item){
    override fun ModelHeaderItemBinding.bind(){
        textView.text = headerText
    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}