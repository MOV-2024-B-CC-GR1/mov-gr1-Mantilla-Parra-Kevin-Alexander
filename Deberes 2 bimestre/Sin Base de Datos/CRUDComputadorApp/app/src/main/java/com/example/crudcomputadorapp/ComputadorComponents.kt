package com.example.crudcomputadorapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.crudcomputadorapp.models.Computador
import com.example.crudcomputadorapp.models.Componente

@Composable
fun ComputadorForm(
    computador: Computador? = null,
    onSave: (Computador) -> Unit,
    onCancel: () -> Unit
) {
    // Fondo con degradado para la interfaz completa
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E88E5), // Azul intenso
                        Color(0xFFBBDEFB)  // Azul claro
                    )
                )
            )
    ) {
        var id by remember { mutableStateOf(computador?.id?.toString() ?: "") }
        var nombre by remember { mutableStateOf(computador?.nombre ?: "") }
        var marca by remember { mutableStateOf(computador?.marca ?: "") }
        var precio by remember { mutableStateOf(computador?.precio?.toString() ?: "") }
        var fechaFabricacion by remember { mutableStateOf(computador?.fechaFabricacion ?: "") }
        var esPortatil by remember { mutableStateOf(computador?.esPortatil ?: false) }
        var componentes = remember { mutableStateListOf<Componente>(*computador?.componentes?.toTypedArray() ?: emptyArray()) }
        var mostrandoDialogoComponente by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (computador == null) "Agregar Computador" else "Editar Computador",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("ID (único)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = marca,
                    onValueChange = { marca = it },
                    label = { Text("Marca") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fechaFabricacion,
                    onValueChange = { fechaFabricacion = it },
                    label = { Text("Fecha de Fabricación") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("¿Es portátil?", color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = esPortatil,
                        onCheckedChange = { esPortatil = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Componentes (${componentes.size})",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(componentes) { componente ->
                        Text(
                            "- ${componente.nombre} (\$${componente.precio} - ${componente.tipo})",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                Button(
                    onClick = { mostrandoDialogoComponente = true },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Agregar Componente", color = MaterialTheme.colorScheme.onPrimary)
                }

                if (mostrandoDialogoComponente) {
                    ComponenteDialog(
                        onDismiss = { mostrandoDialogoComponente = false },
                        onSave = { componente: Componente ->
                            componentes.add(componente)
                            mostrandoDialogoComponente = false
                        }
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            val computadorId = id.toIntOrNull() ?: (1..1000).random()
                            onSave(
                                Computador(
                                    id = computadorId,
                                    nombre = nombre,
                                    marca = marca,
                                    precio = precio.toDoubleOrNull() ?: 0.0,
                                    fechaFabricacion = fechaFabricacion,
                                    esPortatil = esPortatil,
                                    componentes = componentes
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
                    }

                    OutlinedButton(
                        onClick = { onCancel() },
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Cancelar", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun ComponenteDialog(
    onDismiss: () -> Unit,
    onSave: (Componente) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var esReemplazable by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Agregar Componente") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )
                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo") }
                )
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") }
                )
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cantidad") }
                )
                Row {
                    Text("¿Es reemplazable?")
                    Switch(
                        checked = esReemplazable,
                        onCheckedChange = { esReemplazable = it }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    Componente(
                        id = (1..1000).random(),
                        nombre = nombre,
                        tipo = tipo,
                        precio = precio.toDoubleOrNull() ?: 0.0,
                        cantidad = cantidad.toIntOrNull() ?: 0,
                        esReemplazable = esReemplazable
                    )
                )
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}
@Composable
fun ComputadorItem(
    computador: Computador,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "ID: ${computador.id}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Nombre: ${computador.nombre}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "Marca: ${computador.marca}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "Precio: \$${computador.precio}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "Componentes: ${computador.componentes.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun ComputadorList(
    computadores: List<Computador>,
    onEdit: (Computador) -> Unit,
    onDelete: (Int) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(computadores) { computador ->
            ComputadorItem(
                computador = computador,
                onEdit = { onEdit(computador) },
                onDelete = { onDelete(computador.id) }
            )
        }
    }
}

