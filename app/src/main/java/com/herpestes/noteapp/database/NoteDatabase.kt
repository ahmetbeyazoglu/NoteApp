package com.herpestes.noteapp.database

import androidx.room.RoomDatabase

abstract class NoteDatabase: RoomDatabase(){
     abstract fun noteDao(): NoteDao
}