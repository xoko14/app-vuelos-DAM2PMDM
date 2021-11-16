package com.xoquin.vista_ej13

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.xoquin.vista_ej13.vo.BusquedaVuelo

class ActivityBuscar : AppCompatActivity() {
    private var num = 0
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
        val txtReturn: TextInputEditText = findViewById(R.id.txtReturn)

        val txtLog: TextView = findViewById(R.id.txtLog)

        //listeners num
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
            TODO()
        }

        //listener buscar vuelo
        btnSearch.setOnClickListener {
            val trip = findViewById<RadioButton>(tripType).text
            val stops = findViewById<RadioButton>(stopsType).text

            val busqueda = BusquedaVuelo(trip.toString(), txtFrom.text.toString(), txtTo.text.toString(), txtDepart.text.toString(), txtReturn.text.toString(), num, stops.toString())

            val intent = Intent(this, SearchResultsActivity::class.java)
            intent.putExtra("busqueda", busqueda)
            startActivity(intent);
        }
    }


    private fun verificarNum(num: Int): Boolean{
        val toast = Toast(applicationContext)
        if(num<0){
            toast.setText("No puede ser menor que 0")
            toast.show()
            return true
        }
        if(num>19){
            toast.setText("No puede ser mayor que 19")
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