package com.example.passecure.data.repository

import android.content.Context
import androidx.room.Room
import com.example.passecure.data.local.Database
import com.example.passecure.data.model.PassecureItem

class PassItemRepository(applicationContext: Context) {

    private val db = Room.databaseBuilder(
        applicationContext,
        Database::class.java, "passItem-db"
    ).build()

    private val passDao = db.passecureItemDao()

    suspend fun getAll(): List<PassecureItem> {
        return passDao.getAll()
    }

    suspend fun insert(passItem: PassecureItem) {
        passDao.insert(passItem)
    }

    suspend fun update(passItem: PassecureItem) {
        passDao.update(passItem)
    }

    suspend fun delete(passItem: PassecureItem) {
        passDao.delete(passItem)
    }
}