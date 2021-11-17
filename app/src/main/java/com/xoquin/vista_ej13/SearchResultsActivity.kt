package com.xoquin.vista_ej13

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xoquin.vista_ej13.vo.BusquedaVuelo
import com.xoquin.vista_ej13.vo.Vuelo

class SearchResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        intent.getSerializableExtra("")
    }

    fun buscar(busquedaVuelo: BusquedaVuelo): List<Vuelo>{
        TODO()
    }
}