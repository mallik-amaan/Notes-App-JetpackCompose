package com.codinfinity.notes.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: NoteDatabase? = null

    fun getDatabase(context: Context):NoteDatabase{
        return INSTANCE ?: synchronized(this){
            val instance = Room.databaseBuilder(
                context = context.applicationContext,
                NoteDatabase::class.java,
                "notes_database"
            ).build()
            INSTANCE=instance
            instance
        }
    }
}