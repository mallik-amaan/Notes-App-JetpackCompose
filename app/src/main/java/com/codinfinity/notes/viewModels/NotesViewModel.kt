package com.codinfinity.notes.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codinfinity.notes.dao.NoteDao
import com.codinfinity.notes.tables.Note
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(private val dao:NoteDao): ViewModel(){
    val notes = dao.getAllNotes()
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList<Note>()
    )



    fun addNote(note: Note){
        viewModelScope.launch {
            dao.insertNote(note)
        }
    }

    fun removeNote(note:Note){
        viewModelScope.launch {
            dao.deleteNote(note)
        }
    }

    fun updateNote(note:Note){
        viewModelScope.launch {
            dao.updateNote(note)
        }
    }

    fun toggleNoteCompletion(note:Note){
        viewModelScope.launch {
            val updated = note.copy(isCompleted = !note.isCompleted)
            dao.updateNote(updated)
        }
    }
}
