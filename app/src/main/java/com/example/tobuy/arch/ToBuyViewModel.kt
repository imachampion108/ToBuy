package com.example.tobuy.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigationevent.NavigationEventDispatcher
import androidx.room.Dao
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
    var currentSort : HomeViewState.Sort = HomeViewState.Sort.NONE
        set(value) {
            field = value
            updateHomeViewState(itemWithCategoryLiveData.value!!)
        }

    private val _categoriesViewStateLiveData = MutableLiveData<CategoriesViewState>()
    val categoriesViewStateLiveData : LiveData<CategoriesViewState>

        get() = _categoriesViewStateLiveData

    private val _homeViewStateLiveData = MutableLiveData<HomeViewState>()
    val homeViewStateLiveData : LiveData<HomeViewState>
        get() = _homeViewStateLiveData
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

               updateHomeViewState(items)
           }
       }

    }
    fun updateHomeViewState(items : List<ItemWithCategoryEntity>){
        val dataList = ArrayList<HomeViewState.DataItem<*>>()
        when(currentSort){
            HomeViewState.Sort.NONE -> {
                var currentPriority : Int = -1
                items.sortedByDescending {
                    it.itemEntity.priority }.forEach { items ->
                        if (items.itemEntity.priority != currentPriority){
                            currentPriority = items.itemEntity.priority
                            val headerItem = HomeViewState.DataItem(
                                data = getHeaderText(currentPriority),
                                isHeader = true
                            )
                            dataList.add(headerItem)
                        }
                    val dataItem = HomeViewState.DataItem(data = items)
                    dataList.add(dataItem)

                }
            }
            HomeViewState.Sort.CATEGORY -> {
                var currentCategoryId = "no_id"
                items.sortedBy {
                    it.categoryEntity?.name ?: CategoryEntity.Default_VAL
                }.forEach { items ->
                    if (items.itemEntity.id != currentCategoryId){
                        currentCategoryId = items.itemEntity.id
                        val headerItem = HomeViewState.DataItem(
                            data = items.categoryEntity?.id ?: CategoryEntity.Default_VAL,
                            isHeader = true
                        )
                        dataList.add(headerItem)
                    }
                    val data = HomeViewState.DataItem(items)
                    dataList.add(data)
                }
            }
            HomeViewState.Sort.NEWEST -> {
                val headerItem = HomeViewState.DataItem(
                    "Newest",
                    true
                )
                dataList.add(headerItem)

                items.sortedByDescending {
                    it.itemEntity.createdAt
                }.forEach { it ->
                 val dataItem = HomeViewState.DataItem(data = it)
                    dataList.add(dataItem)
                }
            }
            HomeViewState.Sort.OLDEST -> {
                val headerItem = HomeViewState.DataItem(
                    "Oldest",
                    true
                )
                dataList.add(headerItem)
                items.sortedBy {
                    it.itemEntity.createdAt
                }.forEach {
                    val dataItem = HomeViewState.DataItem(data = it)
                    dataList.add(dataItem)
                }
            }
        }
        _homeViewStateLiveData.postValue(
            HomeViewState(
                dataList = dataList,
                isLoading = false,
                sort = currentSort
            )
        )
    }
    private fun getHeaderText(priority: Int): String{
        return when(priority){
            1 -> "LOW"
            2 -> "MEDIUM"
            else -> "HIGH"
        }
    }



    data class HomeViewState(
        val dataList : List<DataItem<*>> = emptyList(),
        val isLoading: Boolean = false,
        val sort : Sort = Sort.NONE
    ){
        data class DataItem<T>(
            val data : T,
            val isHeader : Boolean = false
        )

        enum class Sort(val displayName : String){
            NONE("None"),
            CATEGORY("Category"),
            OLDEST("Oldest"),
            NEWEST("Newest")
        }
    }
    fun onCategorySelected(categoryid : String,showLoading : Boolean = false){
        if (showLoading){
        val loadingViewState = CategoriesViewState(isLoading = true)
        _categoriesViewStateLiveData.value = loadingViewState}
         val categories = categoryLiveData.value ?: return

        val viewStateItemList = ArrayList<CategoriesViewState.item>()
        viewStateItemList.add(CategoriesViewState.item(
          categoryEntity = CategoryEntity.getDefaultCategory(),
            isSelected = categoryid == CategoryEntity.Default_VAL
        ))
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

        fun getSelectedCategoryId(): String{
            return  itemsList.find { it.isSelected }?.categoryEntity?.id ?: CategoryEntity.Default_VAL
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