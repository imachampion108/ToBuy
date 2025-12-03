package com.example.tobuy.arch

import com.example.tobuy.database.AppDatabase
import com.example.tobuy.database.entity.ItemEntity

class ToBuyRepository (private val appDatabase: AppDatabase) {

    fun insertItem(itemEntity: ItemEntity){
        appDatabase.ItemEntityDao().insert(itemEntity)
    }

    fun deleteItem(itemEntity: ItemEntity){
        appDatabase.ItemEntityDao().delete(itemEntity)
    }

    suspend fun getItemList() : List<ItemEntity>{
        return appDatabase.ItemEntityDao().getAll()
    }
}
