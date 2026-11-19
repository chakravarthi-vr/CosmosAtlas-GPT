package org.cosmosatlas.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.cosmosatlas.core.database.entity.ApodEntity

@Dao
interface ApodDao {

    @Query("SELECT * FROM apod_records ORDER BY date DESC")
    fun getAllCached(): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apod_records WHERE isBookmarked = 1 ORDER BY date DESC")
    fun getBookmarked(): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apod_records WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): ApodEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(record: ApodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<ApodEntity>)

    @Query("UPDATE apod_records SET isBookmarked = :bookmarked WHERE date = :date")
    suspend fun updateBookmarkStatus(date: String, bookmarked: Boolean)
}
