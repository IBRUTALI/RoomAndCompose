package com.example.roomrestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class ItemRepository(private val itemDao: ItemDao) {

    private val allData = itemDao.getAll()
    val searchResults = MutableLiveData<List<Item>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun getAllItem(): LiveData<List<Item>> {
        return allData
    }

    fun findItemByTitle(title: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(title)
        }
    }

    fun findItemById(id: Int) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(id)
        }
    }

    fun findItemByRate(rate: Int) {
        itemDao.findByRate(rate)
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

    private suspend fun asyncFind(data: Any): List<Item> =
        when(data){
            is Int -> coroutineScope.async(Dispatchers.IO) {
                return@async itemDao.findById(data)
            }.await()

            is String -> coroutineScope.async(Dispatchers.IO) {
                return@async itemDao.findByTitle(data)
            }.await()
            else -> throw IllegalArgumentException("Unsupported type")
        }
}