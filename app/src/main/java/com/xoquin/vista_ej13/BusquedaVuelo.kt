package com.xoquin.vista_ej13

class BusquedaVuelo(
    var trip: String,
    var from: String,
    var to: String,
    var depart: String,
    var returnT: String,
    var num: Int,
    var stops: String
){
    override fun toString(): String {
        return "$trip | $from -> $to | $depart ${if (returnT == "") "" else "-> $returnT"} | $num | $stops"
    }
}