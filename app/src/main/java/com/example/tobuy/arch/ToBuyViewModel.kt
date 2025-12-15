package com.example.tobuy.arch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tobuy.database.AppDatabase
import com.example.tobuy.database.entity.ItemEntity
import kotlinx.coroutines.launch

class ToBuyViewModel : ViewModel() {
 private lateinit var repository: ToBuyRepository

 val itemsLiveData = MutableLiveData<List<ItemEntity>>()

    fun init(appDatabase: AppDatabase ) {
        repository = ToBuyRepository(appDatabase)


        viewModelScope.launch {
            val items = repository.getItemList()
            itemsLiveData.postValue(items)
        }
    }
      fun insertItem(itemEntity: ItemEntity){
       viewModelScope.launch {
           repository.insertItem(itemEntity)
       }
    }
     fun deleteItem(itemEntity: ItemEntity){
        viewModelScope.launch {  repository.deleteItem(itemEntity)
    }
}}