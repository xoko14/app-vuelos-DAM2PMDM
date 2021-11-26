package com.xoquin.vista_ej13.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.xoquin.vista_ej13.R
import com.xoquin.vista_ej13.adapters.VuelosListAdapter
import com.xoquin.vista_ej13.utils.FirebaseUtils
import com.xoquin.vista_ej13.vo.Vuelo

class VueloDialogFragment(val vuelo: Vuelo): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val viewHolder = ViewHolder()
            val inflater = requireActivity().layoutInflater;
            val tempView = inflater.inflate(R.layout.vuelo_layout, null)

            viewHolder.codVuelo = tempView.findViewById(R.id.cod_vuelo)
            viewHolder.vueloSalida = tempView.findViewById(R.id.vuelo_salida)
            viewHolder.vueloLlegada = tempView.findViewById(R.id.vuelo_llegada)
            viewHolder.vueloTimestamp = tempView.findViewById(R.id.vuelo_timestamp)
            viewHolder.vueloPrecio = tempView.findViewById(R.id.vuelo_precio)
            tempView.tag = viewHolder

            viewHolder.codVuelo?.text = vuelo.cod
            viewHolder.vueloSalida?.text = vuelo.salida
            viewHolder.vueloLlegada?.text = vuelo.destino
            viewHolder.vueloTimestamp?.text = FirebaseUtils.timestampToDate(vuelo.fechaSalida)
            "${vuelo.precio}€".also { viewHolder.vueloPrecio?.text = it }

            builder.setView(tempView)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    class ViewHolder(
        var codVuelo: TextView? = null,
        var vueloSalida: TextView? = null,
        var vueloLlegada: TextView? = null,
        var vueloTimestamp: TextView? = null,
        var vueloPrecio: TextView? = null,
    )
}