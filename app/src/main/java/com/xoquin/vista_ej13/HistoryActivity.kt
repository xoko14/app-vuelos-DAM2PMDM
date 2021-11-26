package com.xoquin.vista_ej13

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
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

        lView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, SearchResultsActivity::class.java)
            intent.putExtra("busqueda", historial[position])
            startActivity(intent);
        }

        var lAdapter: HistorialListAdapter

        val prg: ProgressBar = findViewById(R.id.progressCircleHistory)
        prg.visibility = View.VISIBLE

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

                if(historial.size <=0){
                    findViewById<View>(R.id.nothingFoundHistory).visibility = View.VISIBLE
                }
                lAdapter = HistorialListAdapter(this, historial)
                lView.adapter = lAdapter
            }
            .addOnCompleteListener { prg.visibility = View.GONE }
            .addOnFailureListener {
                Toast.makeText(applicationContext, R.string.err_db, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.deleteHistory -> {
                db.collection("users").document(UserSingleton.username).collection("historial")
                    .get()
                    .addOnSuccessListener {
                        for(document in it){
                            document.reference.delete()
                        }
                        recreate()
                    }
                    .addOnFailureListener{
                        Toast.makeText(applicationContext, R.string.err_db, Toast.LENGTH_SHORT)
                            .show()
                    }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}