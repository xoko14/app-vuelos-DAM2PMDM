package com.xoquin.vista_ej13.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.xoquin.vista_ej13.R
import com.xoquin.vista_ej13.vo.BusquedaVuelo

class HistorialListAdapter(
    private val context: Context,
    private val busqueda: List<BusquedaVuelo>
): BaseAdapter() {


    override fun getCount(): Int {
        return busqueda.size
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
            tempView = inflater.inflate(R.layout.busqueda_layout, parent, false)
            viewHolder.salida = tempView.findViewById(R.id.txtSalida)
            viewHolder.llegada = tempView.findViewById(R.id.txtLlegada)
            viewHolder.numPasajeros = tempView.findViewById(R.id.txtNumPasajeros)
            viewHolder.fecha = tempView.findViewById(R.id.txtFecha)
            tempView.tag = viewHolder
        }
        else{
            tempView = convertView
            viewHolder = tempView.tag as ViewHolder
        }

        viewHolder.salida?.text = busqueda[position].from
        viewHolder.llegada?.text = busqueda[position].to
        "${context.getString(R.string.passenger_num)}: ${busqueda[position].num}".also { viewHolder.numPasajeros?.text = it }
        viewHolder.fecha?.text = busqueda[position].depart
        return tempView

    }

    class ViewHolder(
        var salida: TextView? = null,
        var llegada: TextView? = null,
        var numPasajeros: TextView? = null,
        var fecha: TextView? = null,
    )
}