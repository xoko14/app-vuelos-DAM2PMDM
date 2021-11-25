package com.xoquin.vista_ej13

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xoquin.vista_ej13.adapters.HistorialListAdapter
import com.xoquin.vista_ej13.adapters.ReservasListAdapter
import com.xoquin.vista_ej13.utils.UserSingleton
import com.xoquin.vista_ej13.vo.BusquedaVuelo
import com.xoquin.vista_ej13.vo.Reserva

class HistoryActivity : AppCompatActivity() {
    val db = Firebase.firestore
    val historial: MutableList<BusquedaVuelo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val lView: ListView = findViewById(R.id.viewHistory)
        registerForContextMenu(lView)
        var lAdapter: HistorialListAdapter

        db.collection("users").document(UserSingleton.username).collection("historial")
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { document ->
                    val trip = document.getString("trip") as String
                    val from = document.getString("from") as String
                    val to = document.getString("to") as String
                    val depart = document.getString("depart") as String
                    val returnT = document.getString("returnT") as String
                    val num = Integer.parseInt(document.getLong("num")?.toString() as String)
                    val stops = document.getString("stops") as String
                    historial.add(BusquedaVuelo(trip,from,to,depart,returnT,num,stops))
                }

                lAdapter = HistorialListAdapter(this, historial)
                lView.adapter = lAdapter
            }
    }
}