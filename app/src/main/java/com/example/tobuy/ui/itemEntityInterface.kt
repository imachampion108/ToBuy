package com.example.tobuy.ui

import com.example.tobuy.database.entity.ItemEntity

interface itemEntityInterface {

    fun onDeleteItemEntity(itemEntity: ItemEntity)
    fun onBumpPriority(itemEntity: ItemEntity)
}