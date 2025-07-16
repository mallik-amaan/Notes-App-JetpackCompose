package com.codinfinity.notes.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AddNoteDialog(
    dismissRequest: () -> Unit,
    submitRequest: (String)-> Unit,

){
    var title by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = dismissRequest,
        title = { Text("Add Note") },
        text = {
            Column {
                Text("Add your note")
                Spacer(Modifier.height(8.dp))
                TextField(value = title, onValueChange = {title = it})
                Spacer(Modifier.height(8.dp))
            }
        },
        confirmButton = {
            TextButton(onClick = { submitRequest(title) }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = dismissRequest) {
                Text("Cancel")
            }
        }
    )
}