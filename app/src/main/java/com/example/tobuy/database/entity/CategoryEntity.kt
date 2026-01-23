package com.example.tobuy.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "category_entity")
data class CategoryEntity(
    @PrimaryKey val id : String = "",
    val name : String = ""


) {
    companion object {
        const val Default_VAL = "NONE"

        fun getDefaultCategory() :CategoryEntity{
            return CategoryEntity(Default_VAL,"None")
        }
    }
}