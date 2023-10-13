package com.example.todoapp

import androidx.room.*

@Dao
interface DatabaseAPI {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemEntity?) : Long

    @Delete
    suspend fun delete(item: ItemEntity?)

    @Query("SELECT * FROM ItemEntity")
    suspend fun getAllToDos(): List<ItemEntity>?

    @Query("SELECT * FROM ItemEntity WHERE id LIKE :id")
    suspend fun getById(id: Int): List<ItemEntity>?

    @Query("DELETE FROM ItemEntity")
    suspend fun deleteAll()

}