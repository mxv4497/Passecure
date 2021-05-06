package com.example.passecure.data.local

import androidx.room.*
import androidx.room.Database
import com.example.passecure.data.model.PassecureItem

@Dao
interface PassecureItemDao {
    @Query("SELECT * FROM passecureItem ORDER BY name ASC")
    suspend fun getAll(): List<PassecureItem>

    @Query("SELECT * FROM passecureItem where id = :id LIMIT 1")
    suspend fun findOne(id: Int): PassecureItem?

    @Insert
    suspend fun insert(passItem: PassecureItem)

    @Update
    suspend fun update(passItem: PassecureItem)

    @Delete
    suspend fun delete(passItem: PassecureItem)
}

@Database(entities = [PassecureItem::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun passecureItemDao(): PassecureItemDao
}