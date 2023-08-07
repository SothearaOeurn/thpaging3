package com.theara.paging3.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.theara.paging3.data.response.PagingRes

@Database(entities = [PagingRes::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun PagingDao(): PagingDao

}