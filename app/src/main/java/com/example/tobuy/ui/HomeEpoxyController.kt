package com.example.tobuy.ui
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.tobuy.R
import androidx.viewbinding.ViewBinding
import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.databinding.ModelItemEntityBinding

class HomeEpoxyController(private val itemEntityInterface: itemEntityInterface) : EpoxyController() {
    var isLoading : Boolean = true
        set(value){
            field = value
            if (field) {
                requestModelBuild()
            }
        }

    var itemEntityList = ArrayList<ItemEntity>()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    override fun buildModels() {
        if (isLoading) {
            // loading state
            // todo loading state
            LoadingEpoxyModel().id("loading_state").addTo(this)
        return
        }
        if (itemEntityList.isEmpty()){
            // empty state
            return
        }

        itemEntityList.forEach { item ->
            ItemEntityEpoxyModel(item,itemEntityInterface).id(item.id).addTo(this)
        }
    }

    data class ItemEntityEpoxyModel(
            val itemEntity: ItemEntity,
           val itemEntityInterface : itemEntityInterface
            ) : ViewBindingKotlinModel<ModelItemEntityBinding>(R.layout.model_item_entity){
        override fun ModelItemEntityBinding.bind(){
            titleTextView.text = itemEntity.title
            if (itemEntity.description == null){
                descriptionTextView.isGone = true
            }else{
                 descriptionTextView.isVisible = true
                descriptionTextView.text = itemEntity.description

            }
          deleteImageView.setOnClickListener {
                itemEntityInterface.onDeleteItemEntity(itemEntity)
          }
            priorityTextView.setOnClickListener {
                itemEntityInterface.onBumpPriority(itemEntity)
            }
            val colorRes = when(itemEntity.priority){
                1 -> android.R.color.holo_green_dark
                2 -> android.R.color.holo_orange_dark
                3 -> android.R.color.holo_red_dark
                else -> android.R.color.holo_purple
            }
            priorityTextView.setBackgroundColor(ContextCompat.getColor(root.context,colorRes))
        } }
}