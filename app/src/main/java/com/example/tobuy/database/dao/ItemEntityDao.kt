package com.example.tobuy.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tobuy.database.entity.ItemEntity

@Dao
interface ItemEntityDao {

    @Query("SELECT * FROM itemEntity")
    fun getAll(): List<ItemEntity>

    @Insert
    fun getAll(vararg items : ItemEntity)

    @Delete
    fun delete(itmes : ItemEntity)

}