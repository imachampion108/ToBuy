package com.example.tobuy.arch

import androidx.lifecycle.LiveData
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

    private val _categoriesViewStateLiveData = MutableLiveData<CategoriesViewState>()
    val categoriesViewStateLiveData : LiveData<CategoriesViewState>

        get() = _categoriesViewStateLiveData
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
    fun onCategorySelected(categoryid : String){
        val loadingViewState = CategoriesViewState(isLoading = true)
        _categoriesViewStateLiveData.value = loadingViewState
         val categories = categoryLiveData.value ?: return

        val viewStateItemList = ArrayList<CategoriesViewState.item>()
        categories.forEach {
            viewStateItemList.add(
                CategoriesViewState.item(
                    categoryEntity = it,
                    isSelected = it.id == categoryid
                )
            )
        }
        val viewState = CategoriesViewState(itemsList = viewStateItemList)
        _categoriesViewStateLiveData.postValue(viewState)
    }

    data class CategoriesViewState(
        val isLoading : Boolean = false,
        val itemsList: List<item> = emptyList()
    ){
        data class item(
            val categoryEntity: CategoryEntity = CategoryEntity(),
            val isSelected : Boolean = false
        )
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