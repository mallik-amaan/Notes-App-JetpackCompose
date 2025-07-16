package com.codinfinity.notes.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codinfinity.notes.dao.NoteDao
import com.codinfinity.notes.tables.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase(){
    abstract fun noteDao() : NoteDao

}