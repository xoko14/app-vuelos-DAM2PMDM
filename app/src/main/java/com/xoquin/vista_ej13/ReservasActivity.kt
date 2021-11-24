package com.xoquin.vista_ej13

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xoquin.vista_ej13.adapters.ReservasListAdapter
import com.xoquin.vista_ej13.adapters.VuelosListAdapter
import com.xoquin.vista_ej13.utils.UserSingleton
import com.xoquin.vista_ej13.vo.Reserva

class ReservasActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservas)

        val lView: ListView = findViewById(R.id.reservas_list)
        var lAdapter: ReservasListAdapter

        var reservas: MutableList<Reserva> = ArrayList()

        db.collection("users").document(UserSingleton.username).collection("reservas")
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { document ->
                    reservas.add(Reserva(document.id, document.getString("cod_vuelo") as String, document.getLong("num_tickets") as Long, document.getLong("precio") as Long))
                }

                lAdapter = ReservasListAdapter(this, reservas)
                lView.adapter = lAdapter
            }

    }
}