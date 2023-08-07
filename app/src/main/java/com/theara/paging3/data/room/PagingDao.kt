package com.theara.paging3.data.room

import androidx.room.*
import com.theara.paging3.data.response.PagingRes
import kotlinx.coroutines.flow.Flow

@Dao
interface PagingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg loginTableModel: PagingRes)

    @Query("SELECT * FROM Paging")
    fun getProData(): List<PagingRes>

    @Delete
    fun delete(user: PagingRes?)

    @Query("DELETE FROM Paging")
    suspend fun deleteAll()

    @Update
    fun updateUsers(vararg users: PagingRes)
}