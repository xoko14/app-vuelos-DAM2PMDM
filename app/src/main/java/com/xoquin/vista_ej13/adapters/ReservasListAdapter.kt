package com.xoquin.vista_ej13.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.xoquin.vista_ej13.R
import com.xoquin.vista_ej13.utils.FirebaseUtils
import com.xoquin.vista_ej13.vo.Reserva
import com.xoquin.vista_ej13.vo.Vuelo

class ReservasListAdapter(
    private val context: Context,
    private val reservas: List<Reserva>
): BaseAdapter() {


    override fun getCount(): Int {
        return reservas.size
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
            tempView = inflater.inflate(R.layout.reserva_layout, parent, false)
            viewHolder.bookNum = tempView.findViewById(R.id.txtBookNum)
            viewHolder.flightCode = tempView.findViewById(R.id.txtFlightCode)
            viewHolder.price = tempView.findViewById(R.id.txtPrice)
            tempView.tag = viewHolder
        }
        else{
            tempView = convertView
            viewHolder = tempView.tag as ViewHolder
        }

        "${context.getString(R.string.booking_num)} ${reservas[position].id}".also { viewHolder.bookNum?.text = it }
        viewHolder.flightCode?.text = reservas[position].cod
        "${context.getString(R.string.total)}: ${reservas[position].precio}€".also { viewHolder.price?.text = it }
        return tempView

    }

    class ViewHolder(
        var bookNum: TextView? = null,
        var flightCode: TextView? = null,
        var price: TextView? = null,
    )
}