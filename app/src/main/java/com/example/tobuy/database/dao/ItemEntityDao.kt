package com.example.tobuy.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tobuy.database.entity.ItemEntity

@Dao
interface ItemEntityDao {

    @Query("SELECT * FROM item_entity")
    suspend fun getAll(): List<ItemEntity>

    @Insert
    suspend fun insert(items : ItemEntity)

    @Delete
    suspend fun delete(items: ItemEntity)

}