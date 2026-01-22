package com.example.tobuy.ui

import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.ui.HomeEpoxyController
import com.google.android.material.color.MaterialColors

fun EpoxyController.addHeaderModel(headerText: String){
    HeaderEpoxyModel(headerText).id(headerText).addTo(this)

}
@ColorInt
fun View.getAttrColor(attrResId : Int) : Int{
return MaterialColors.getColor(this,attrResId)
}