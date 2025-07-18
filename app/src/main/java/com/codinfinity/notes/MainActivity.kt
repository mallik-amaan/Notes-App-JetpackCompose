package com.codinfinity.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.codinfinity.notes.database.DatabaseProvider
import com.codinfinity.notes.screens.NotesScreen
import com.codinfinity.notes.ui.theme.NotesTheme
import com.codinfinity.notes.viewModels.NotesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = DatabaseProvider.getDatabase(applicationContext)
        val dao = db.noteDao()
        val viewModel = NotesViewModel(dao)
        setContent {
            NotesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NotesScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

