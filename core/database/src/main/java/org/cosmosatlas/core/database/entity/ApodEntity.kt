package org.cosmosatlas.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.cosmosatlas.core.model.ApodRecord

@Entity(tableName = "apod_records")
data class ApodEntity(
    @PrimaryKey val date: String,
    val title: String,
    val explanation: String,
    val url: String,
    val hdUrl: String?,
    val mediaType: String,
    val copyright: String?,
    val isBookmarked: Boolean,
    val cachedTimestamp: Long = System.currentTimeMillis()
) {
    fun toDomain(): ApodRecord = ApodRecord(
        date = date,
        title = title,
        explanation = explanation,
        url = url,
        hdUrl = hdUrl,
        mediaType = mediaType,
        copyright = copyright,
        isBookmarked = isBookmarked
    )

    companion object {
        fun fromDomain(record: ApodRecord): ApodEntity = ApodEntity(
            date = record.date,
            title = record.title,
            explanation = record.explanation,
            url = record.url,
            hdUrl = record.hdUrl,
            mediaType = record.mediaType,
            copyright = record.copyright,
            isBookmarked = record.isBookmarked
        )
    }
}
