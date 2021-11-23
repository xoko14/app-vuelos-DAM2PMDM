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
import com.xoquin.vista_ej13.dao.VueloDAO
import com.xoquin.vista_ej13.vo.BusquedaVuelo
import com.xoquin.vista_ej13.vo.Vuelo
import java.text.SimpleDateFormat

class SearchResultsActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val vueloDAO = VueloDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        val busqueda: BusquedaVuelo = intent.getSerializableExtra("busqueda") as BusquedaVuelo
        val departTimestamp = Timestamp(SimpleDateFormat("dd/MM/yyyy").parse(busqueda.depart))
        val departTimestampMax = Timestamp(departTimestamp.seconds+24*60*60, departTimestamp.nanoseconds)

        var results: List<Vuelo> = ArrayList()

        val lView: ListView = findViewById(R.id.vuelos_list)

        lView.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(applicationContext, "jaja si ${results[i].cod}", Toast.LENGTH_SHORT).show()
        }

        var lAdapter: VuelosListAdapter

        val prg: ProgressBar = findViewById(R.id.progressCircleSearch)
        prg.visibility = View.VISIBLE
        val vuelosRef = db.collection("vuelos")
        vuelosRef.whereGreaterThanOrEqualTo("fecha_salida", departTimestamp)
            .whereLessThan("fecha_salida", departTimestampMax)
            .whereEqualTo("salida", busqueda.from)
            .whereEqualTo("destino", busqueda.to)
            .get()
            .addOnSuccessListener { documents ->
                results = vueloDAO.convert(documents)
                lAdapter = VuelosListAdapter(this, results)
                lView.adapter = lAdapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext, "u dun goofed", Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                prg.visibility = View.GONE
            }
    }



}