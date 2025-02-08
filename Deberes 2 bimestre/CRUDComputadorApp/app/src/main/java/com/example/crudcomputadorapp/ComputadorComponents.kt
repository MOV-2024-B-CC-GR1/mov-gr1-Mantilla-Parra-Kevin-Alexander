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
import com.example.crudcomputadorapp.models.DataManager
import com.example.crudcomputadorapp.ui.MapaScreen // ✅ Se importa el mapa
import kotlinx.coroutines.launch

@Composable
fun ComputadorForm(
    computador: Computador? = null,
    dataManager: DataManager,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var mostrandoDialogoComponente by remember { mutableStateOf(false) }
    var mostrandoMapa by remember { mutableStateOf(false) } // ✅ Nuevo estado para abrir el mapa

    var nombre by remember { mutableStateOf(computador?.nombre ?: "") }
    var marca by remember { mutableStateOf(computador?.marca ?: "") }
    var precio by remember { mutableStateOf(computador?.precio?.toString() ?: "") }
    var fechaFabricacion by remember { mutableStateOf(computador?.fechaFabricacion ?: "") }
    var esPortatil by remember { mutableStateOf(computador?.esPortatil ?: false) }
    var latitud by remember { mutableStateOf(computador?.latitud ?: 0.0) } // ✅ Se agrega latitud
    var longitud by remember { mutableStateOf(computador?.longitud ?: 0.0) } // ✅ Se agrega longitud

    val componentes by dataManager.obtenerComponentes(computador?.id ?: 0).collectAsState(initial = emptyList())

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1565C0), Color(0xFF42A5F5))))
            .padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = if (computador == null) "Agregar Computador" else "Editar Computador",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = marca, onValueChange = { marca = it }, label = { Text("Marca") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = fechaFabricacion, onValueChange = { fechaFabricacion = it }, label = { Text("Fecha de Fabricación") }, modifier = Modifier.fillMaxWidth())

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿Es portátil?", color = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = esPortatil, onCheckedChange = { esPortatil = it })
            }

            // ✅ Botón para seleccionar ubicación en el mapa
            if (mostrandoMapa) {
                MapaScreen(
                    latitudInicial = latitud,
                    longitudInicial = longitud,
                    modoSeleccion = true
                ) { nuevaLat, nuevaLng ->
                    latitud = nuevaLat
                    longitud = nuevaLng
                    mostrandoMapa = false
                }
            } else {
                Button(
                    onClick = { mostrandoMapa = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
                ) {
                    Text("Seleccionar Ubicación", color = Color.White)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        val computadorNuevo = Computador(
                            id = computador?.id ?: 0,
                            nombre = nombre,
                            marca = marca,
                            precio = precio.toDoubleOrNull() ?: 0.0,
                            fechaFabricacion = fechaFabricacion,
                            esPortatil = esPortatil,
                            latitud = latitud, // ✅ Guardar latitud
                            longitud = longitud // ✅ Guardar longitud
                        )

                        coroutineScope.launch {
                            if (computador == null) {
                                dataManager.agregarComputador(computadorNuevo)
                            } else {
                                dataManager.actualizarComputador(computadorNuevo)
                            }
                            onSave()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Guardar", color = Color.White)
                }

                OutlinedButton(onClick = { onCancel() }) {
                    Text("Cancelar", color = Color.Red)
                }
            }

            if (computador != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { mostrandoDialogoComponente = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
                ) {
                    Text("Agregar Componente", color = Color.White)
                }

                Text("Componentes:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))

                LazyColumn {
                    items(componentes) { componente ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(4.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("🔹 ${componente.nombre} - ${componente.tipo}", fontWeight = FontWeight.Bold)
                                Text("Precio: \$${componente.precio}")
                                Text("Cantidad: ${componente.cantidad}")
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrandoDialogoComponente) {
        AgregarComponenteDialog(computadorId = computador?.id, dataManager = dataManager, onDismiss = { mostrandoDialogoComponente = false })
    }
}
@Composable
fun AgregarComponenteDialog(
    computadorId: Int?,
    dataManager: DataManager,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var esReemplazable by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Agregar Componente") },
        text = {
            Column {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del componente") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = tipo, onValueChange = { tipo = it }, label = { Text("Tipo") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = cantidad, onValueChange = { cantidad = it }, label = { Text("Cantidad") }, modifier = Modifier.fillMaxWidth())

                Row {
                    Text("¿Es reemplazable?")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(checked = esReemplazable, onCheckedChange = { esReemplazable = it })
                }

                errorMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (computadorId == null) {
                        errorMessage = "Error: Computador no válido"
                        return@Button
                    }

                    val precioValor = precio.toDoubleOrNull() ?: 0.0
                    val cantidadValor = cantidad.toIntOrNull() ?: 1

                    if (nombre.isNotEmpty() && tipo.isNotEmpty() && precioValor > 0) {
                        val nuevoComponente = Componente(
                            nombre = nombre,
                            tipo = tipo,
                            precio = precioValor,
                            cantidad = cantidadValor,
                            esReemplazable = esReemplazable,
                            computadorId = computadorId
                        )

                        coroutineScope.launch {
                            dataManager.agregarComponente(nuevoComponente)
                            onDismiss()
                        }
                    } else {
                        errorMessage = "Por favor, completa todos los campos correctamente."
                    }
                }
            ) {
                Text("Agregar")
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
    var mostrandoMapa by remember { mutableStateOf(false) } // 🔥 Estado para mostrar/ocultar el mapa

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFBBDEFB),
            contentColor = Color.Black
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("ID: ${computador.id}", fontWeight = FontWeight.Bold)
            Text("Nombre: ${computador.nombre}")
            Text("Marca: ${computador.marca}")
            Text("Precio: \$${computador.precio}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }

            // ✅ Botón para ver/ocultar la ubicación
            Button(
                onClick = { mostrandoMapa = !mostrandoMapa }, // 🔥 Alternar la visibilidad del mapa
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
            ) {
                Text(if (mostrandoMapa) "Ocultar Mapa" else "Ver Ubicación", color = Color.White)
            }

            // ✅ Si mostrandoMapa es true, mostramos el mapa
            if (mostrandoMapa) {
                MapaScreen(
                    latitudInicial = computador.latitud ?: 0.0, // Si es null, usa 0.0
                    longitudInicial = computador.longitud ?: 0.0, // Si es null, usa 0.0
                    modoSeleccion = false
                )
            }
        }
    }
}
