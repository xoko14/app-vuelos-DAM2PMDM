package com.xoquin.vista_ej13.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment

class DatePickerFragment: DialogFragment(){
    private var changed: DatePickerDialog.OnDateSetListener? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val d = DatePickerDialog(requireContext())
        d.setOnDateSetListener(changed)
        return d
    }
    fun setOnDateChanged(listener: DatePickerDialog.OnDateSetListener){
        changed = listener
    }
}