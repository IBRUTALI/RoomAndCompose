package com.example.roomrestore

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.*

class MainViewModel(application: Application) : ViewModel() {

    private val allData: LiveData<List<Item>>
    private val itemDB = MainDatabase.getDB(application)
    private val itemDao = itemDB.itemDao()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    val searchResults: MutableLiveData<List<Item>>

    init {
        allData = itemDao.getAll()
        searchResults = MutableLiveData<List<Item>>()
    }

    fun getAllItem(): LiveData<List<Item>> {
        return allData
    }

    fun findItemByTitle(title: String): LiveData<List<Item>> {
            return itemDao.findByTitle(title).asLiveData()
    }

    fun findItemById(id: Int) {
        coroutineScope.launch(Dispatchers.Main) {
        searchResults.value = asyncFind(id).await()
        }
    }

    fun findItemByRate(rate: Int): LiveData<List<Item>> {
        return itemDao.findByRate(rate).asLiveData()
    }

    fun insertItem(item: Item) {
        coroutineScope.launch(Dispatchers.IO) {
            itemDao.insertData(item)
        }
    }

    fun deleteItem(item: Item) {
        coroutineScope.launch(Dispatchers.IO) {
            itemDao.deleteData(item)
        }
    }

    private fun asyncFind(id: Int): Deferred<List<Item>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async itemDao.findById(id)
        }
}