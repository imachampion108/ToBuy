package com.example.tobuy.arch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tobuy.database.AppDatabase
import com.example.tobuy.database.entity.CategoryEntity
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.database.entity.ItemWithCategoryEntity
import kotlinx.coroutines.launch

class ToBuyViewModel : ViewModel() {
    private lateinit var repository: ToBuyRepository

    val itemsLiveData = MutableLiveData<List<ItemEntity>>()
    val categoryLiveData = MutableLiveData<List<CategoryEntity>>()
    val itemWithCategoryLiveData = MutableLiveData<List<ItemWithCategoryEntity>>()
    val transactionCompleteLiveData = MutableLiveData<Event<Boolean>>()
// initialize our flow connectivity to the db for item entities and categories entity
    fun init(appDatabase: AppDatabase) {
        repository = ToBuyRepository(appDatabase)

        viewModelScope.launch {
            repository.getItemList().collect { items ->
                itemsLiveData.postValue(items)

            }
        }
            viewModelScope.launch {
                repository.getCategory().collect { categoryEntities ->
                    categoryLiveData.postValue(categoryEntities)
                }
            }
       viewModelScope.launch {
           repository.getAllItemWithCategoryEntities().collect { items ->
               itemWithCategoryLiveData.postValue(items)
           }
       }

    }
  // region itementity
    fun insertItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            repository.insertItem(itemEntity)

            transactionCompleteLiveData.postValue(Event(true))
        }
    }
    fun updateItem(itemEntity: ItemEntity){
        viewModelScope.launch {
            repository.updateItem(itemEntity)
            transactionCompleteLiveData.postValue(Event(true))
        }
    }

    fun deleteItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            repository.deleteItem(itemEntity)
        }
    }
    fun insertCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            repository.insertCategory(categoryEntity)

            transactionCompleteLiveData.postValue(Event(true))
        }
    }
    fun updateCategory(categoryEntity: CategoryEntity){
        viewModelScope.launch {
            repository.updateCategory(categoryEntity)
            transactionCompleteLiveData.postValue(Event(true))
        }
    }

    fun deleteCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            repository.deleteCategory(categoryEntity)
        }
    }
}