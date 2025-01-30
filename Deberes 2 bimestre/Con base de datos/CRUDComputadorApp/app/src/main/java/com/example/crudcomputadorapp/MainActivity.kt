package com.example.crudcomputadorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.crudcomputadorapp.models.Computador
import com.example.crudcomputadorapp.ui.theme.CRUDComputadorAppTheme
import androidx.compose.ui.Alignment


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CRUDComputadorAppTheme {
                CRUDScreen()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CRUDScreen() {
    val computadores = remember { mutableStateListOf<Computador>() } // Lista de computadores reactiva
    var computadorParaEditar by remember { mutableStateOf<Computador?>(null) }
    var mostrandoFormulario by remember { mutableStateOf(false) }

    // Función para agregar un computador
    fun agregarComputador(computador: Computador) {
        computadores.add(computador)
        mostrandoFormulario = false
    }

    // Función para actualizar un computador
    fun actualizarComputador(computador: Computador) {
        val index = computadores.indexOfFirst { it.id == computador.id }
        if (index != -1) {
            computadores[index] = computador
        }
        mostrandoFormulario = false
    }

    // Función para eliminar un computador
    fun eliminarComputador(id: Int) {
        computadores.removeIf { it.id == id }
    }

    // Fondo con degradado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2196F3), // Azul
                        Color(0xFFE3F2FD)  // Azul claro
                    )
                )
            )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "CRUD de Computadores",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color(0xFF2196F3),
                        titleContentColor = Color.White
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        computadorParaEditar = null
                        mostrandoFormulario = true
                    },
                    containerColor = Color(0xFF64B5F6),
                    contentColor = Color.White
                ) {
                    Text("+", style = MaterialTheme.typography.titleLarge)
                }
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (mostrandoFormulario) {
                    ComputadorForm(
                        computador = computadorParaEditar,
                        onSave = { computador ->
                            if (computadorParaEditar == null) {
                                agregarComputador(computador)
                            } else {
                                actualizarComputador(computador)
                            }
                        },
                        onCancel = { mostrandoFormulario = false }
                    )
                } else {
                    if (computadores.isEmpty()) {
                        // Mensaje de lista vacía
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No hay computadores registrados",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.DarkGray
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            ComputadorList(
                                computadores = computadores,
                                onEdit = { computador ->
                                    computadorParaEditar = computador
                                    mostrandoFormulario = true
                                },
                                onDelete = { id -> eliminarComputador(id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
