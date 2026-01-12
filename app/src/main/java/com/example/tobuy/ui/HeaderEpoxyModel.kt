package com.example.tobuy.ui

import com.example.tobuy.R
import com.example.tobuy.databinding.ModelHeaderItemBinding


data class HeaderEpoxyModel(
    val headerText: String
) : ViewBindingKotlinModel<ModelHeaderItemBinding>(R.layout.model_header_item){
    override fun ModelHeaderItemBinding.bind(){
        textView.text = headerText
    }
}