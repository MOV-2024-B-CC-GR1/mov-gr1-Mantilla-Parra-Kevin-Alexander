package com.example.crudcomputadorapp

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.crudcomputadorapp.models.Computador
import com.example.crudcomputadorapp.models.DataManager
import com.example.crudcomputadorapp.ui.theme.CRUDComputadorAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CRUDComputadorAppTheme {
                CRUDScreen(applicationContext)
            }
        }
    }
}

@Composable
fun CRUDScreen(context: Context) {
    val dataManager = remember { DataManager(context) }
    var computadorParaEditar by remember { mutableStateOf<Computador?>(null) }
    var mostrandoFormulario by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun eliminarComputador(computador: Computador) {
        coroutineScope.launch {
            dataManager.eliminarComputador(computador)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    computadorParaEditar = null
                    mostrandoFormulario = true
                },
                containerColor = Color(0xFF03A9F4),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFF1565C0), Color(0xFF42A5F5))))
                .padding(16.dp)
        ) {
            if (mostrandoFormulario) {
                ComputadorForm(
                    computador = computadorParaEditar,
                    dataManager = dataManager,
                    onSave = { mostrandoFormulario = false },
                    onCancel = { mostrandoFormulario = false }
                )
            } else {
                Text(
                    text = "Lista de Computadores",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )

                val computadores by dataManager.obtenerComputadores().collectAsState(initial = emptyList())

                LazyColumn {
                    items(computadores) { computador ->
                        ComputadorItem(
                            computador = computador,
                            onEdit = {
                                computadorParaEditar = computador
                                mostrandoFormulario = true
                            },
                            onDelete = { eliminarComputador(computador) }
                        )
                    }
                }
            }
        }
    }
}