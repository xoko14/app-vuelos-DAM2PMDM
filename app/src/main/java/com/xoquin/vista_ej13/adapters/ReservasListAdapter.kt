package com.xoquin.vista_ej13.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.xoquin.vista_ej13.R
import com.xoquin.vista_ej13.vo.Reserva

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
            viewHolder.reservaImg = tempView.findViewById(R.id.reservaImg)

            tempView.tag = viewHolder
        }
        else{
            tempView = convertView
            viewHolder = tempView.tag as ViewHolder
        }

        "${context.getString(R.string.booking_num)} ${reservas[position].id}".also { viewHolder.bookNum?.text = it }
        viewHolder.flightCode?.text = reservas[position].cod
        "${context.getString(R.string.total)} (x${reservas[position].tickets}): ${reservas[position].precio}€".also { viewHolder.price?.text = it }

        if(reservas[position].primeraClase){
            viewHolder.reservaImg?.setColorFilter(ContextCompat.getColor(context, R.color.premium_yellow), android.graphics.PorterDuff.Mode.SRC_IN)
        }

        return tempView

    }

    class ViewHolder(
        var bookNum: TextView? = null,
        var flightCode: TextView? = null,
        var price: TextView? = null,
        var reservaImg: ImageView? = null,
    )


}