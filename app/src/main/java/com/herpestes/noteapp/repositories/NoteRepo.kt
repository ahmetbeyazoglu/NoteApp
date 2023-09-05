package com.herpestes.noteapp.repositories

import com.herpestes.noteapp.database.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepo {

    suspend fun addNote(note:NoteEntity)
    suspend fun deleteNote(note:NoteEntity)
    suspend fun getNotes():Flow<List<NoteEntity>>

}