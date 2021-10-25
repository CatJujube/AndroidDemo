package com.jube.androiddemo.Widgets.Dialog.BottomSheetFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jube.androiddemo.R

class BottomSheetFragment: BottomSheetDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.bottom_sheet_dialog_layout,container,false)
        bindView(mView)
        return mView
    }

    private fun bindView(view:View){
        val copy = view.findViewById<LinearLayout>(R.id.copyLinearLayout)
        val share = view.findViewById<LinearLayout>(R.id.shareLinearLayout)
        val upload = view.findViewById<LinearLayout>(R.id.uploadLinearLayout)
        val download = view.findViewById<LinearLayout>(R.id.download)
        val delete = view.findViewById<LinearLayout>(R.id.delete)
        copy!!.setOnClickListener {
            Toast.makeText(
                context,
                "Copy is Clicked ",
                Toast.LENGTH_LONG
            ).show()
        }

        share!!.setOnClickListener {
            Toast.makeText(
                context,
                "Share is Clicked",
                Toast.LENGTH_LONG
            ).show()
        }

        upload!!.setOnClickListener {
            Toast.makeText(
                context,
                "Upload is Clicked",
                Toast.LENGTH_LONG
            ).show()
        }

        download!!.setOnClickListener {
            Toast.makeText(
                context,
                "Download is Clicked",
                Toast.LENGTH_LONG
            ).show()
        }

        delete!!.setOnClickListener {
            Toast.makeText(
                context,
                "Delete is Clicked",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}