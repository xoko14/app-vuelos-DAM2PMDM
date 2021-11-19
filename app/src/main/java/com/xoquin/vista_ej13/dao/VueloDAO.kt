package com.xoquin.vista_ej13.dao

import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xoquin.vista_ej13.vo.Vuelo
import java.util.*
import kotlin.collections.ArrayList


class VueloDAO: DAO<Vuelo> {
    private val db = Firebase.firestore

    override fun getOne(): Vuelo {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Vuelo> {
        val vuelos: MutableList<Vuelo> = ArrayList()
        var complete: Boolean = false
        db.collection("vuelos")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val cod = document.id
                    val salida = document.get("salida") as String
                    val destino = document.get("destino") as String
                    val numPasajeros = document.get("num_pasajeros") as Long
                    val maxPasajeros = document.get("max_pasajeros") as Long
                    val fechaSalida = document.getTimestamp("fecha_salida") as Timestamp
                    val precio = document.get("precio") as Long
                    vuelos.add(Vuelo(cod, salida, destino, numPasajeros, maxPasajeros, fechaSalida, precio))
                }
            }
            .addOnFailureListener { exception ->
                throw exception
            }
            .addOnCompleteListener { complete = true }

        //
        while(!complete){
            var esperar = 0
        }
        return vuelos
    }
}