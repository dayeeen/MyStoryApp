package com.dayeeen.mystoryapp.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dayeeen.mystoryapp.data.response.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStoryItem>)

    @Query("SELECT * FROM listStoryItem")
    fun getAllStories(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM listStoryItem")
    suspend fun deleteAll()
}