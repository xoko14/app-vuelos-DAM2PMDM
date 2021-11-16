package com.xoquin.vista_ej13.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.xoquin.vista_ej13.R
import com.xoquin.vista_ej13.utils.FirebaseUtils
import com.xoquin.vista_ej13.vo.Vuelo

class VuelosListAdapter(
    private val context: Context,
    private val vuelos: List<Vuelo>
): BaseAdapter() {


    override fun getCount(): Int {
        return vuelos.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder = ViewHolder()
        val tempView: View

        if(convertView == null){
            val inflater: LayoutInflater = LayoutInflater.from(context)
            tempView = inflater.inflate(R.layout.vuelo_layout, parent, false)
            viewHolder.codVuelo = tempView.findViewById(R.id.cod_vuelo)
            viewHolder.vueloSalida = tempView.findViewById(R.id.vuelo_salida)
            viewHolder.vueloLlegada = tempView.findViewById(R.id.vuelo_llegada)
            viewHolder.vueloTimestamp = tempView.findViewById(R.id.vuelo_timestamp)
            viewHolder.vueloPrecio = tempView.findViewById(R.id.vuelo_precio)
            tempView.tag = viewHolder
        }
        else{
            tempView = convertView
            viewHolder = tempView.tag as ViewHolder
        }

        viewHolder.codVuelo?.text = vuelos[position].cod
        viewHolder.vueloSalida?.text = vuelos[position].salida
        viewHolder.vueloLlegada?.text = vuelos[position].destino
        viewHolder.vueloTimestamp?.text = FirebaseUtils.timestampToDate(vuelos[position].fechaSalida)
        "${vuelos[position].precio}€".also { viewHolder.vueloPrecio?.text = it }
        return tempView

    }

    class ViewHolder(
        var codVuelo: TextView? = null,
        var vueloSalida: TextView? = null,
        var vueloLlegada: TextView? = null,
        var vueloTimestamp: TextView? = null,
        var vueloPrecio: TextView? = null,
    )
}