package com.example.roomrestore.ui

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.roomrestore.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM items")
    fun getAll(): LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE title = :title")
    fun findByTitle(title: String):  Flow<List<Item>>

    @Query("SELECT * FROM items WHERE id = :id")
    fun findById(id: Int): List<Item>

    @Query("SELECT * FROM items WHERE rate = :rate")
    fun findByRate(rate: Int): Flow<List<Item>>

    @Insert
    fun insertData(item: Item)

    @Delete
    fun deleteData(item: Item)

}