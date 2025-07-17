package com.codinfinity.notes.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codinfinity.notes.dao.NoteDao
import com.codinfinity.notes.tables.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(private val dao:NoteDao): ViewModel(){

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    val notes = dao.getAllNotes()
        .onEach { _isLoading.value = false } // loading ends when data first arrives
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

    fun updateNote(note:Note,title:String?,version: Int){
        viewModelScope.launch {
            if(title.isNullOrEmpty()){
                val updated = note.copy(version = version)
                dao.updateNote(updated)
            }
            else {
                val updated = note.copy(title = title, version = version)
                dao.updateNote(updated)
        }
        }
    }

    fun toggleNoteCompletion(note:Note){
        viewModelScope.launch {
            val updated = note.copy(isCompleted = !note.isCompleted)
            dao.updateNote(updated)
        }
    }
}
