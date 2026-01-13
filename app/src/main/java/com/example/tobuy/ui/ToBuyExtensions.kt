package com.example.tobuy.ui

import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.ui.HomeEpoxyController

fun EpoxyController.addHeaderModel(headerText: String){
    HeaderEpoxyModel(headerText).id(headerText).addTo(this)

}
