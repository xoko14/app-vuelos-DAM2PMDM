package com.xoquin.vista_ej13.vo

import java.io.Serializable

class BusquedaVuelo(
    private var trip: String,
    private var from: String,
    private var to: String,
    private var depart: String,
    private var returnT: String,
    private var num: Int,
    private var stops: String
): Serializable{
    override fun toString(): String {
        return "$trip | $from -> $to | $depart ${if (returnT == "") "" else "-> $returnT"} | $num | $stops"
    }
}