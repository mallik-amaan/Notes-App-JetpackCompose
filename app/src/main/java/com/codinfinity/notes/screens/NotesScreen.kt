package com.codinfinity.notes.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.codinfinity.notes.viewModels.Note
import com.codinfinity.notes.viewModels.NotesViewModel
import com.codinfinity.notes.widgets.AddNoteDialog
import com.codinfinity.notes.widgets.EditNoteDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
    fun NotesScreen(modifier: Modifier,viewModel: NotesViewModel) {
    var showDialog by remember { mutableStateOf(false) };
    var showEditDialog by remember { mutableStateOf(false) };
    var noteToEdit by remember { mutableStateOf<Note?>(null) }
    val notes = viewModel.notes
    var count = 0
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text("Notes App") },

                )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ){innerPadding ->
       if (notes.size == 0)
           Box(modifier = Modifier
               .fillMaxSize()
               .padding(innerPadding),
               contentAlignment = Alignment.Center
                ) {
               Text("No notes found.")
           } else
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(notes, key = {it.id to it.title}) { note ->
                NoteWidget(
                    title = note.title,
                    isCompleted = note.isCompleted,
                    checkChanged = {
                     viewModel.toggleNoteCompletion(note)
                    },
                    onDelete = {
                        viewModel.removeNote(note)
                    },
                    onEdit = {
                        noteToEdit = note
                        showEditDialog = true
                    }
                )
                Spacer(Modifier.height(6.dp))
            }
        }
    }
    if(showDialog){
        AddNoteDialog(
            dismissRequest = {showDialog = false},
            submitRequest = {
                viewModel.addNote(Note(
                    id = count++,
                    isCompleted = false,
                    title = it
                ))
                showDialog = false
            }
        )
    }
    if(showEditDialog){
        noteToEdit?.let {note ->
            EditNoteDialog(
                dismissRequest = {showEditDialog = false},
                submitRequest = {title ->
                    viewModel.updateNote(note = note, title = title)
                    showEditDialog = false
                },
                note = note
            )
        }
    }
}

@Composable
fun NoteWidget(title:String, isCompleted: Boolean, onDelete: ()->Unit,onEdit:()->Unit,checkChanged: (Boolean) -> Unit){
 val dismissBoxState = rememberSwipeToDismissBoxState(
     confirmValueChange = {
        when(it){
            SwipeToDismissBoxValue.EndToStart ->{
                onDelete()
                true
            }
            SwipeToDismissBoxValue.StartToEnd ->{
                onEdit()
                true
            }
            else -> false
        }
     }
 )
    if (dismissBoxState.currentValue != SwipeToDismissBoxValue.Settled) {
        LaunchedEffect(dismissBoxState.currentValue) {
            dismissBoxState.reset()
        }
    }
 SwipeToDismissBox(
     state = dismissBoxState,
     backgroundContent = {
         if(dismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart)
         Box(
             Modifier
                 .fillMaxSize()
                 .padding(horizontal = 16.dp)
                 .clip(RoundedCornerShape(20.dp))
                 .background(color = Color.Red)
                 .border(
                     border = BorderStroke(width = 2.dp, color = Color.Black,),
                     shape = RoundedCornerShape(20.dp)
                 )
         ) {
             Text("Delete...", Modifier.align(Alignment.Center))
         }
         else if(dismissBoxState.dismissDirection == SwipeToDismissBoxValue.StartToEnd){
             Box(
                 Modifier
                     .fillMaxSize()
                     .padding(horizontal = 16.dp)
                     .clip(RoundedCornerShape(20.dp))
                     .background(Color.Green)
                     .border(
                         border = BorderStroke(width = 2.dp, color = Color.Black,),
                         shape = RoundedCornerShape(20.dp)
                     )
             ) {
                 Text("Edit...", Modifier.align(Alignment.Center))
             }
         }
     }
 ) {
    Box(
     modifier = Modifier
         .fillMaxWidth()
         .padding(horizontal = 16.dp)
         .clip(RoundedCornerShape(20.dp))
         .background(color = Color.LightGray)
         .border(
             border = BorderStroke(width = 2.dp, color = Color.Black,),
             shape = RoundedCornerShape(20.dp)
         )
 ){
     Row(
         modifier = Modifier
             .fillMaxWidth()
             .padding(10.dp),
         horizontalArrangement = Arrangement.SpaceBetween
     ) {
         Text(title,
             style = TextStyle(
                 color = Color.Black
             )
         )
         Checkbox(
             checked = isCompleted,
             onCheckedChange = checkChanged,
             colors = CheckboxDefaults.colors(

             )
         )
     }
 }
}
}