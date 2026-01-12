package com.example.tobuy.ui

import android.R.attr.text

fun HomeEpoxyController.addHeaderModel(headerText: String){
    HeaderEpoxyModel(headerText).id(text).addTo(this)

}
