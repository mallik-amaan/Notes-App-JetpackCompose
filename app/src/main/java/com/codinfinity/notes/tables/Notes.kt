package com.codinfinity.notes.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val title:String,
    val isCompleted:Boolean,
    )