package com.codinfinity.notes.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codinfinity.notes.tables.Note
import com.codinfinity.notes.viewModels.NotesViewModel
import com.codinfinity.notes.widgets.AddNoteDialog
import com.codinfinity.notes.widgets.EditNoteDialog
import kotlinx.coroutines.Delay
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
    fun NotesScreen(modifier: Modifier,viewModel: NotesViewModel) {
    var showDialog by remember { mutableStateOf(false) };
    var showEditDialog by remember { mutableStateOf(false) };
    var noteToEdit by remember { mutableStateOf<Note?>(null) }
    val notes by viewModel.notes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text("Notes App")}
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
        if(isLoading){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color . White,
                    strokeWidth = 3.dp
                )
            }
        }
       else if (notes.isEmpty())
           Box(modifier = Modifier
               .fillMaxSize()
               .padding(innerPadding),
               contentAlignment = Alignment.Center
                ) {
               Text("No notes found.")
           } else
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(notes, key = {"${it.id} - ${it.title} - ${it.version}"}) { note ->
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
            submitRequest = {title ->
                viewModel.addNote(Note(
                    isCompleted = false,
                    title = title
                ))
                showDialog = false
            }
        )
    }
    if(showEditDialog){
        noteToEdit?.let {note ->
            EditNoteDialog(
                dismissRequest = {version ->
                    viewModel.updateNote(
                        note = note, version = version, title =null
                    )
                    showEditDialog = false},
                submitRequest = {title,version ->
                    viewModel.updateNote(note = note,title = title,version = version)
                    showEditDialog = false
                },
                note = note
            )
        }
    }
}

@Composable
fun NoteWidget(title:String, isCompleted: Boolean, onDelete: ()->Unit,onEdit:()->Unit,checkChanged: (Boolean) -> Unit){
    var isEditing by remember { mutableStateOf(false) }
    val dismissBoxState = rememberSwipeToDismissBoxState(
     initialValue = SwipeToDismissBoxValue.Settled,
     confirmValueChange = {
        when(it){
            SwipeToDismissBoxValue.EndToStart ->{
                onDelete()
                true
            }
            SwipeToDismissBoxValue.StartToEnd ->{
                onEdit()
                isEditing = true
                true
            }
            else -> false
        }
     }
 )
    LaunchedEffect(dismissBoxState.currentValue) {
        if (dismissBoxState.currentValue != SwipeToDismissBoxValue.Settled) {
            dismissBoxState.reset()
        }
    }

    SwipeToDismissBox(
     state = dismissBoxState,
     backgroundContent = {
         var currentState = dismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart
         Box(
             Modifier
                 .fillMaxSize()
                 .padding(horizontal = 16.dp)
                 .clip(RoundedCornerShape(10.dp))
                 .background(color = if (currentState) Color.Red else Color.Green)
                 .border(
                     border = BorderStroke(width = 2.dp, color = Color.Black,),
                     shape = RoundedCornerShape(10.dp)
                 ),
             contentAlignment = if (currentState)
                 Alignment.CenterEnd else Alignment.CenterStart
         ) {
             if(currentState)
            Icon(
                Icons.Default.Delete, contentDescription = "delete", Modifier.padding(horizontal = 16.dp),tint = Color.White)
             else
                 Icon(
                     Icons.Default.Edit, contentDescription = "edit",Modifier.padding(horizontal = 16.dp), tint = Color.White)

         }
     }
 ) {
    Box(
     modifier = Modifier
         .fillMaxWidth()
         .padding(horizontal = 16.dp)
         .clip(RoundedCornerShape(10.dp))
         .background(color = Color.DarkGray)
         .border(
             border = BorderStroke(width = 2.dp, color = Color.Black,),
             shape = RoundedCornerShape(10.dp)
         )
 ){
     Row(
         modifier = Modifier
             .fillMaxWidth()
             .padding(10.dp),
         horizontalArrangement = Arrangement.SpaceBetween
     ) {
         Text(title,
             Modifier.padding(8.dp),
             style = TextStyle(
                 color = Color.White,
                 fontSize = 20.sp,
                 textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
             )
         )
         Checkbox(
             checked = isCompleted,
             onCheckedChange = checkChanged,
             colors = CheckboxDefaults.colors(
                    checkedColor = Color.Green,
                 uncheckedColor = Color.Black,
                 checkmarkColor = Color.White

             )
         )
     }
 }
}
}