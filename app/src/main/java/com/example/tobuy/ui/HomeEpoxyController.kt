package com.example.tobuy.ui
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.tobuy.R
import androidx.viewbinding.ViewBinding
import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.arch.ToBuyViewModel
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.database.entity.ItemWithCategoryEntity
import com.example.tobuy.databinding.ModelEmptyStateBinding
import com.example.tobuy.databinding.ModelHeaderItemBinding
import com.example.tobuy.databinding.ModelItemEntityBinding

class HomeEpoxyController(private val itemEntityInterface: itemEntityInterface) : EpoxyController() {


     var viewState : ToBuyViewModel.HomeViewState = ToBuyViewModel.HomeViewState(isLoading = true)
       set(value){
            field = value
                requestModelBuild()
            }



    override fun buildModels() {
        if (viewState.isLoading) {
            // loading state
            // todo loading state
            LoadingEpoxyModel().id("loading_state").addTo(this)
        return
        }
        if (viewState.dataList.isEmpty()){
            // empty state
            EmptyStateEpoxyModel().id("empty_state").addTo(this)
            return
        }
         viewState.dataList.forEach { dataItem ->
             if (dataItem.isHeader){
                 addHeaderModel(dataItem.data as String)
                 return@forEach
             }

             val itemWithCategoryEntity = dataItem.data as ItemWithCategoryEntity
             ItemEntityEpoxyModel(itemWithCategoryEntity,itemEntityInterface)
                 .id(itemWithCategoryEntity.itemEntity.id)
                 .addTo(this)
         }
    }
    data class ItemEntityEpoxyModel(
            val itemEntity: ItemWithCategoryEntity,
           val itemEntityInterface : itemEntityInterface
            ) : ViewBindingKotlinModel<ModelItemEntityBinding>(R.layout.model_item_entity){
        override fun ModelItemEntityBinding.bind(){
            titleTextView.text = itemEntity.itemEntity.title
            if (itemEntity.itemEntity.description == null){
                descriptionTextView.isGone = true
            }else{
                 descriptionTextView.isVisible = true
                descriptionTextView.text = itemEntity.itemEntity.description

            }
        //  deleteImageView.setOnClickListener {
          //      itemEntityInterface.onDeleteItemEntity(itemEntity)
          //}
            priorityTextView.setOnClickListener {
                itemEntityInterface.onBumpPriority(itemEntity.itemEntity)
            }
            val colorRes = when(itemEntity.itemEntity.priority){
                1 -> android.R.color.holo_green_dark
                2 -> android.R.color.holo_orange_dark
                3 -> android.R.color.holo_red_dark
                else -> android.R.color.holo_purple
            }
            val color = ContextCompat.getColor(root.context,colorRes)
            priorityTextView.setBackgroundColor(color)
            root.setStrokeColor(ColorStateList.valueOf(color))

            root.setOnClickListener {
                itemEntityInterface.OnSelectedItem(itemEntity.itemEntity)
            }
            categoryConstraintTextView.text = itemEntity.categoryEntity?.name
        } }
     class EmptyStateEpoxyModel : ViewBindingKotlinModel<ModelEmptyStateBinding>(R.layout.model_empty_state){

         override fun ModelEmptyStateBinding.bind(){}

    }

}