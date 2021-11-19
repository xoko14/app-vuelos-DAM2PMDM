package com.xoquin.vista_ej13.vo

import com.google.firebase.Timestamp

class Vuelo(
    val cod: String,
    val salida:String,
    val destino:String,
    val numPasajeros: Long,
    val maxPasajeros: Long,
    val fechaSalida: Timestamp,
    val precio: Long,
){
    override fun toString(): String {
        return "$cod, $precio"
    }
}

