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
import com.xoquin.vista_ej13.fragments.OptionDialogFragment
import com.xoquin.vista_ej13.utils.UserSingleton
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
        val vuelosRef = db.collection("vuelos")
        vuelosRef.whereGreaterThanOrEqualTo("fecha_salida", departTimestamp)
            .whereLessThan("fecha_salida", departTimestampMax)
            .whereEqualTo("salida", busqueda.from)
            .whereEqualTo("destino", busqueda.to)
            .get()
            .addOnSuccessListener { documents ->
                results = vueloDAO.convert(documents)

                if(results.size <=0){
                    findViewById<View>(R.id.nothingFoundResults).visibility = View.VISIBLE
                }

                lAdapter = VuelosListAdapter(this, results)
                lView.adapter = lAdapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext, getString(R.string.err_db), Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                prg.visibility = View.GONE
            }
    }



}