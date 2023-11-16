package com.example.todoapp.data.items.api

import androidx.room.*
import com.example.todoapp.data.items.ItemEntity

@Dao
interface DatabaseApi {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemEntity) : Long

    @Delete
    suspend fun delete(item: ItemEntity)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM items")
    suspend fun getAllToDos(): List<ItemEntity>?

    @Query("SELECT * FROM items WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): ItemEntity?

}