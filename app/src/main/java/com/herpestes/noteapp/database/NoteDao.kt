package com.herpestes.noteapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {

    @Insert
    fun addNote(note:NoteEntity)
    @Query("SELECT * FROM `notes`")
    suspend fun getNotes():Flow<List<NoteEntity>>

    @Delete
    fun deleteNote(note:NoteEntity)

}