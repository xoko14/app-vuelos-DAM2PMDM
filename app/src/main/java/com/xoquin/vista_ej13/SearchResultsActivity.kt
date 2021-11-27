package com.xoquin.vista_ej13

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xoquin.vista_ej13.adapters.VuelosListAdapter
import com.xoquin.vista_ej13.fragments.OptionDialogFragment
import com.xoquin.vista_ej13.utils.UserSingleton
import com.xoquin.vista_ej13.vo.BusquedaVuelo
import com.xoquin.vista_ej13.vo.Vuelo
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchResultsActivity : AppCompatActivity() {
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        val busqueda: BusquedaVuelo = intent.getSerializableExtra("busqueda") as BusquedaVuelo
        val departTimestamp = Timestamp(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(busqueda.depart) as Date)
        val departTimestampMax = Timestamp(departTimestamp.seconds+24*60*60, departTimestamp.nanoseconds)

        val results: MutableList<Vuelo> = ArrayList()

        val lView: ListView = findViewById(R.id.vuelos_list)

        lView.setOnItemClickListener { _, _, i, _ ->
            val dialog = OptionDialogFragment()
            val vuelo: Vuelo = results[i]
            dialog.setPositiveClickListener{_,_ ->
                val reserva = hashMapOf(
                    "cod_vuelo" to vuelo.cod,
                    "precio" to vuelo.precio*busqueda.num,
                    "num_tickets" to busqueda.num,
                    "primera_clase" to false
                )

                db.collection("users").document(UserSingleton.username).collection("reservas").document(System.currentTimeMillis().toString())
                    .set(reserva)
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, getString(R.string.err_db) , Toast.LENGTH_SHORT).show()
                    }
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, getString(R.string.booking_success), Toast.LENGTH_SHORT).show()
                    }
            }
            dialog.setNegativeClickListener{_,_ -> }
            dialog.show(supportFragmentManager, "confirmation")
        }

        var lAdapter: VuelosListAdapter

        val prg: ProgressBar = findViewById(R.id.progressCircleSearch)
        prg.visibility = View.VISIBLE

        db.collection("vuelos").whereGreaterThanOrEqualTo("fecha_salida", departTimestamp)
            .whereLessThan("fecha_salida", departTimestampMax)
            .whereEqualTo("salida", busqueda.from)
            .whereEqualTo("destino", busqueda.to)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val cod = document.id
                    val salida = document.get("salida") as String
                    val destino = document.get("destino") as String
                    val numPasajeros = document.get("num_pasajeros") as Long
                    val maxPasajeros = document.get("max_pasajeros") as Long
                    val fechaSalida = document.getTimestamp("fecha_salida") as Timestamp
                    val precio = document.get("precio") as Long
                    results.add(Vuelo(cod, salida, destino, numPasajeros, maxPasajeros, fechaSalida, precio))
                }

                if(results.isEmpty()){
                    findViewById<View>(R.id.nothingFoundResults).visibility = View.VISIBLE
                }

                lAdapter = VuelosListAdapter(this, results)
                lView.adapter = lAdapter
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, getString(R.string.err_db), Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                prg.visibility = View.GONE
            }
    }



}