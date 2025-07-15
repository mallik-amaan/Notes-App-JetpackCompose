package com.codinfinity.notes.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class NotesViewModel: ViewModel(){
    var notes = mutableStateListOf<Note>(
    )

        private set
    fun addNote(note: Note){
        notes.add(note);
    }

    fun removeNote(note:Note){
        notes.remove(note);
    }

    fun updateNote(note:Note,title:String){
        val index = notes.indexOf(note)
        if (index != -1) {
            val updated = note.copy(title = title)
            notes[index] = updated
        }
    }
    fun toggleNoteCompletion(note: Note) {
        val index = notes.indexOf(note)
        if (index != -1) {
            val updated = note.copy(isCompleted = !note.isCompleted)
            notes[index] = updated
        }
    }

}

data class Note(
    var id:Int,
    var title:String,
    var isCompleted:Boolean,

)