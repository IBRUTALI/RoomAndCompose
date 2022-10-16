package com.example.roomrestore

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

class MainViewModel(application: Application) : ViewModel() {

    private val allData: LiveData<List<Item>>
    private val db = MainDatabase.getDB(application)

    init {
        allData = db.itemDao().getAll().asLiveData()
    }

    fun getAllItem(): LiveData<List<Item>> {
        return allData
    }

    fun findItemByTitle(title: String): LiveData<List<Item>> {
            return db.itemDao().findByTitle(title).asLiveData()
    }

    fun findItemById(id: Int): LiveData<List<Item>> {
        return db.itemDao().findById(id).asLiveData()
    }

    fun findItemByRate(rate: Int): LiveData<List<Item>> {
        return db.itemDao().findByRate(rate).asLiveData()
    }

    fun insertItem(item: Item) {
        Thread {
            db.itemDao().insertData(item)
        }.start()
    }

    fun deleteItem(item: Item) {
        Thread {
            db.itemDao().deleteData(item)
        }.start()
    }
}