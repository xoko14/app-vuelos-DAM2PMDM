package com.xoquin.vista_ej13

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xoquin.vista_ej13.adapters.ReservasListAdapter
import com.xoquin.vista_ej13.adapters.VuelosListAdapter
import com.xoquin.vista_ej13.dao.VueloDAO
import com.xoquin.vista_ej13.fragments.VueloDialogFragment
import com.xoquin.vista_ej13.utils.FirebaseUtils
import com.xoquin.vista_ej13.utils.UserSingleton
import com.xoquin.vista_ej13.vo.Reserva
import com.xoquin.vista_ej13.vo.Vuelo

class ReservasActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private var reservas: MutableList<Reserva> = ArrayList()
    private var vueloDAO = VueloDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservas)

        val lView: ListView = findViewById(R.id.reservas_list)

        registerForContextMenu(lView)
        var lAdapter: ReservasListAdapter

        val prg: ProgressBar = findViewById(R.id.progressCircleReservas)
        prg.visibility = View.VISIBLE
        db.collection("users").document(UserSingleton.username).collection("reservas")
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { document ->
                    reservas.add(Reserva(
                        document.id,
                        document.getString("cod_vuelo") as String,
                        document.getLong("num_tickets") as Long,
                        document.getLong("precio") as Long,
                        document.getBoolean("primera_clase") as Boolean)
                    )
                }

                if(reservas.size <=0){
                    findViewById<View>(R.id.nothingFoundReservas).visibility = View.VISIBLE
                }

                lAdapter = ReservasListAdapter(this, reservas)
                lView.adapter = lAdapter
            }
            .addOnCompleteListener { prg.visibility = View.GONE }

        lView.setOnItemClickListener { adapterView, view, i, l ->
            db.collection("vuelos").document(reservas[i].cod)
                .get()
                .addOnSuccessListener { document ->
                    val cod = document.id
                    val salida = document.get("salida") as String
                    val destino = document.get("destino") as String
                    val numPasajeros = document.get("num_pasajeros") as Long
                    val maxPasajeros = document.get("max_pasajeros") as Long
                    val fechaSalida = document.getTimestamp("fecha_salida") as Timestamp
                    val precio = document.get("precio") as Long
                    val vuelo = Vuelo(cod, salida, destino, numPasajeros, maxPasajeros, fechaSalida, precio)

                    val dialog = VueloDialogFragment(vuelo)
                    dialog.show(supportFragmentManager, "vuelo")
                }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.vuelo_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info: AdapterView.AdapterContextMenuInfo = item.menuInfo as AdapterView.AdapterContextMenuInfo
        when(item.itemId){
            R.id.itemAddReminder -> {
                db.collection("vuelos").document(reservas[info.position].cod)
                    .get()
                    .addOnSuccessListener {
                        val timestamp = it.getTimestamp("fecha_salida") as Timestamp
                        val intent = Intent(Intent.ACTION_INSERT)
                        intent.setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.Events.TITLE, "${getString(R.string.calendar_title)} ${it.getString("destino")}")
                            .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000)
                        startActivity(intent)
                    }
            }
            R.id.itemAirportDepart -> {
                db.collection("vuelos").document(reservas[info.position].cod)
                    .get()
                    .addOnSuccessListener { vuelo ->
                        db.collection("aeropuertos").document(vuelo.getString("salida") as String)
                            .get()
                            .addOnSuccessListener { aeropuerto ->
                                val geo = aeropuerto.getGeoPoint("geo_loc") as GeoPoint
                                val uri = Uri.parse("geo:${geo.latitude},${geo.longitude}")
                                val intent = Intent(Intent.ACTION_VIEW , uri)
                                startActivity(intent)
                            }
                    }
            }
            R.id.itemAirportLand -> {
                db.collection("vuelos").document(reservas[info.position].cod)
                    .get()
                    .addOnSuccessListener { vuelo ->
                        db.collection("aeropuertos").document(vuelo.getString("destino") as String)
                            .get()
                            .addOnSuccessListener { aeropuerto ->
                                val geo = aeropuerto.getGeoPoint("geo_loc") as GeoPoint
                                val uri = Uri.parse("geo:${geo.latitude},${geo.longitude}")
                                val intent = Intent(Intent.ACTION_VIEW , uri)
                                startActivity(intent)
                            }
                    }
            }
            R.id.itemCancelFlight ->{
                db.collection("users").document(UserSingleton.username).collection("reservas").document(reservas[info.position].id)
                    .delete()
                    .addOnSuccessListener {
                        recreate()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, getString(R.string.err_db), Toast.LENGTH_SHORT).show()
                    }
            }
            R.id.itemUpgradeClass ->{
                if(reservas[info.position].primeraClase){
                    Toast.makeText(this, getString(R.string.already_first_class), Toast.LENGTH_SHORT).show()
                }
                else{
                    db.collection("users").document(UserSingleton.username).collection("reservas").document(reservas[info.position].id)
                        .update("precio", reservas[info.position].precio+(100*reservas[info.position].tickets),
                            "primera_clase", true)
                        .addOnSuccessListener {
                            recreate()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, getString(R.string.err_db), Toast.LENGTH_SHORT).show()
                        }
                }
            }
            else -> super.onContextItemSelected(item)
        }
        return true
    }
}