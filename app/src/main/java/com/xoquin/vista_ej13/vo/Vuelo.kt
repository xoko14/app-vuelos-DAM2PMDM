package com.xoquin.vista_ej13.vo

import com.google.firebase.Timestamp

class Vuelo(
    val cod: String,
    val salida:String,
    val destino:String,
    val numPasajeros: Int,
    val maxPasajeros: Int,
    val fechaSalida: Timestamp,
    val precio: Int,
)

