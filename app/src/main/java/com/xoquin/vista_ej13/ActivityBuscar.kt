package com.xoquin.vista_ej13

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xoquin.vista_ej13.fragments.DatePickerFragment
import com.xoquin.vista_ej13.utils.UserSingleton
import com.xoquin.vista_ej13.vo.BusquedaVuelo
import java.text.SimpleDateFormat
import java.util.*

class ActivityBuscar : AppCompatActivity() {
    private var db = Firebase.firestore

    private var num = 1
    private var tripType:Int = 0
    private var stopsType:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar)

        //definir elementos de la vista
        val txtNum: TextView = findViewById(R.id.txtNumber)
        val btnPlus: ImageButton = findViewById(R.id.btnPlus)
        val btnMinus: ImageButton = findViewById(R.id.btnMinus)

        val chkRoundtrip: RadioButton = findViewById(R.id.chkRoundtrip)
        val chkOneWay: RadioButton = findViewById(R.id.chkOneWay)
        val radioGroup1: List<RadioButton> = listOf(chkRoundtrip, chkOneWay)
        tripType = chkRoundtrip.id

        val chkNonStop: RadioButton = findViewById(R.id.chkNonstop)
        val chkOneStop: RadioButton = findViewById(R.id.chkOneStop)
        val chk2orMore: RadioButton = findViewById(R.id.chk2orMore)
        val radioGroup2: List<RadioButton> = listOf(chkNonStop, chkOneStop, chk2orMore)
        stopsType = chkNonStop.id

        val btnHistory: Button = findViewById(R.id.btnHistory)
        val btnSearch: Button = findViewById(R.id.searchFlights)

        val txtFrom: TextInputEditText = findViewById(R.id.txtFrom)
        val txtTo: TextInputEditText = findViewById(R.id.txtTo)
        val txtDepart: TextInputEditText = findViewById(R.id.txtDepart)
        txtDepart.keyListener = null
        val txtReturn: TextInputEditText = findViewById(R.id.txtReturn)
        txtReturn.keyListener = null


        //listeners num pasajeros
        btnPlus.setOnClickListener {
            num++
            num = if (verificarNum(num)) --num
            else num
            txtNum.text = num.toString()
        }
        btnMinus.setOnClickListener {
            num--
            num = if (verificarNum(num)) ++num
            else num
            txtNum.text = num.toString()
        }

        //listeners grupo checkbox 1
        chkRoundtrip.setOnClickListener {
            groupSet(radioGroup1, chkRoundtrip.id)
            tripType = chkRoundtrip.id
            findViewById<TextInputLayout>(R.id.layReturn).visibility = View.VISIBLE
        }
        chkOneWay.setOnClickListener {
            groupSet(radioGroup1, chkOneWay.id)
            tripType = chkOneWay.id
            findViewById<TextInputLayout>(R.id.layReturn).visibility = View.INVISIBLE
            txtReturn.setText("")
        }

        //listeners grupo checkbox 2
        chkNonStop.setOnClickListener {
            groupSet(radioGroup2, chkNonStop.id)
            stopsType = chkNonStop.id
        }
        chkOneStop.setOnClickListener {
            groupSet(radioGroup2, chkOneStop.id)
            stopsType = chkOneStop.id
        }
        chk2orMore.setOnClickListener {
            groupSet(radioGroup2, chk2orMore.id)
            stopsType = chk2orMore.id
        }

        //listener history
        btnHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        //listener buscar vuelo
        btnSearch.setOnClickListener {
            val trip = findViewById<RadioButton>(tripType).text
            val stops = findViewById<RadioButton>(stopsType).text

            if(txtDepart.text.isNullOrBlank() || txtFrom.text.isNullOrBlank() || txtFrom.text.isNullOrBlank()){
                Toast.makeText(this, getString(R.string.empty_fields_warning), Toast.LENGTH_SHORT).show()
            }
            else {

                val busqueda = BusquedaVuelo(
                    trip.toString(),
                    txtFrom.text.toString(),
                    txtTo.text.toString(),
                    txtDepart.text.toString(),
                    txtReturn.text.toString(),
                    num,
                    stops.toString()
                )

                val historial = hashMapOf(
                    "trip" to busqueda.trip,
                    "from" to busqueda.from,
                    "to" to busqueda.to,
                    "depart" to busqueda.depart,
                    "returnT" to busqueda.returnT,
                    "num" to busqueda.num,
                    "stops" to busqueda.stops
                )

                db.collection("users").document(UserSingleton.username).collection("historial")
                    .document(System.currentTimeMillis().toString())
                    .set(historial)
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, R.string.err_db, Toast.LENGTH_SHORT)
                            .show()
                    }

                val intent = Intent(this, SearchResultsActivity::class.java)
                intent.putExtra("busqueda", busqueda)
                startActivity(intent)
            }
        }

        //listeners abrir di??logos de escoller hora
        txtDepart.setOnClickListener {
            val datePicker = DatePickerFragment()
            datePicker.setOnDateChanged { _, year, month, dayOfMonth ->
                val c: Calendar = Calendar.getInstance()
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, month)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                txtDepart.setText(sdf.format(c.time).toString())
            }
            datePicker.show(supportFragmentManager, "selector fecha")
        }

        txtReturn.setOnClickListener {
            val datePicker = DatePickerFragment()
            datePicker.setOnDateChanged { _, year, month, dayOfMonth ->
                val c: Calendar = Calendar.getInstance()
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, month)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                txtReturn.setText(sdf.format(c.time).toString())
            }
            datePicker.show(supportFragmentManager, "selector fecha")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.seeBookings  -> { //ver reservas
                val intent = Intent(this, ReservasActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun verificarNum(num: Int): Boolean{
        val toast = Toast(applicationContext)
        if(num<1){
            toast.setText(R.string.no_less_than_1)
            toast.show()
            return true
        }
        if(num>19){
            toast.setText(R.string.no_more_than_19)
            toast.show()
            return true
        }
        return false
    }

    private fun groupSet(group: List<RadioButton>, id: Int){
        for(item in group){
            if(item.id != id){
                item.isChecked = false
            }
        }
    }
}