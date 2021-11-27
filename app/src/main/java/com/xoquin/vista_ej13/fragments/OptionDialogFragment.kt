package com.xoquin.vista_ej13.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.xoquin.vista_ej13.R

class OptionDialogFragment: DialogFragment() {
    private var positiveClickListener: DialogInterface.OnClickListener? = null
    fun setPositiveClickListener(l: DialogInterface.OnClickListener?){
        positiveClickListener = l
    }

    private var negativeClickListener: DialogInterface.OnClickListener? = null
    fun setNegativeClickListener(l: DialogInterface.OnClickListener?){
        negativeClickListener = l
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.book_confirmation)
                .setPositiveButton(R.string.yes, positiveClickListener)
                .setNegativeButton(R.string.no, negativeClickListener)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}