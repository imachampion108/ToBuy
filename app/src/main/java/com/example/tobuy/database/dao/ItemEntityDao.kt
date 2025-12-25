package com.example.tobuy.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tobuy.database.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemEntityDao {

    @Query("SELECT * FROM item_entity")
    fun getAll(): Flow<List<ItemEntity>>

    @Insert
    suspend fun insert(items : ItemEntity)

    @Delete
    suspend fun delete(items: ItemEntity)

}