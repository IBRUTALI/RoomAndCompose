package com.example.roomrestore

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.*

class MainViewModel(application: Application) : ViewModel() {

    val allData: LiveData<List<Item>>
    private val repository: ItemRepository
    val searchResults: MutableLiveData<List<Item>>

    init {
        val itemDB = MainDatabase.getDB(application)
        val itemDao = itemDB.itemDao()
        repository = ItemRepository(itemDao)

        allData = repository.getAllItem()
        searchResults = repository.searchResults
    }

    fun insertItem(item: Item) {
        repository.insertItem(item)
    }

    fun findById(id: Int) {
        repository.findItemById(id)
    }

    fun findByTitle(title: String) {
        repository.findItemByTitle(title)
    }

    fun deleteItem(item: Item) {
        repository.deleteItem(item)
    }
}