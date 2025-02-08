package com.example.crudcomputadorapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("MissingPermission")
@Composable
fun MapaScreen(
    latitudInicial: Double?,
    longitudInicial: Double?,
    modoSeleccion: Boolean = false,
    onUbicacionSeleccionada: ((Double, Double) -> Unit)? = null
) {
    val ubicacionInicial = if (latitudInicial != null && longitudInicial != null && latitudInicial != 0.0 && longitudInicial != 0.0) {
        LatLng(latitudInicial, longitudInicial)
    } else {
        LatLng(-12.0464, -77.0428) // 📌 Ubicación por defecto (Lima, Perú)
    }

    var marcadorPosicion by remember { mutableStateOf(ubicacionInicial) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(marcadorPosicion, 15f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                if (modoSeleccion) {
                    marcadorPosicion = latLng
                }
            }
        ) {
            Marker(
                state = MarkerState(position = marcadorPosicion),
                title = if (modoSeleccion) "Nueva Ubicación" else "Ubicación Guardada"
            )
        }

        if (modoSeleccion && onUbicacionSeleccionada != null) {
            Button(
                onClick = { onUbicacionSeleccionada(marcadorPosicion.latitude, marcadorPosicion.longitude) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Guardar Ubicación")
            }
        }
    }
}
