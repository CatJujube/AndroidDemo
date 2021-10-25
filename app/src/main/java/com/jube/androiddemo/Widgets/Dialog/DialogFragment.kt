package com.jube.androiddemo.Widgets.Dialog

import android.os.Bundle
import android.util.Log
import com.jube.androiddemo.Widgets.ListFragment.ListFragment
import com.jube.androiddemo.Widgets.ListFragment.ListFragmentItem
import android.content.DialogInterface
import android.text.format.DateFormat.is24HourFormat
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jube.androiddemo.R
import android.widget.LinearLayout
import android.widget.Toast
import com.jube.androiddemo.Widgets.Dialog.BottomSheetFragment.BottomSheetFragment
import com.jube.androiddemo.Widgets.Dialog.BottomSheetTimeRangePicker.BottomSheetTimeRangePicker
import com.jube.androiddemo.Widgets.Dialog.BottomSheetTimeRangePicker.OnTimeRangeSelectedListener
import java.text.DateFormat

class DialogFragment : ListFragment() {
    companion object{
        const val TAG = "WidgetsMain_Log"
    }
    private val tagBottomSheetTimeRangePicker = "tagBottomSheetTimeRangePicker"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG,"DialogFragment->onCreate()")
        initData(listOf())
        bindView()
    }

    private fun bindView(){
        add(ListFragmentItem("Alter Dialog",object :ListFragmentItem.IListFragmentCallback{
            override fun onClick() {
                SimpleDialog()
            }}))
        add(ListFragmentItem("Button Dialog", object :ListFragmentItem.IListFragmentCallback{
            override fun onClick() {
                ButtonDialog()
            }}))
        add(ListFragmentItem("Bottom Sheet Dialog", object :ListFragmentItem.IListFragmentCallback{
            override fun onClick() {
                BottomSheetDialog()
            }}))
        add(ListFragmentItem("Bottom Sheet Fragment", object :ListFragmentItem.IListFragmentCallback{
            override fun onClick() {
                val blankFragment = BottomSheetFragment()
                childFragmentManager.let { blankFragment.show(it, blankFragment.tag) }
            }}))

        add(ListFragmentItem("Time Picker Fragment", object :ListFragmentItem.IListFragmentCallback{
            override fun onClick() {
                val blankFragment =
                    BottomSheetTimeRangePicker
                        .tabLabels("开始时间","结束时间")
                        .doneButtonLabel("确定")
                        .startTimeInitialHour(2)
                        .startTimeInitialMinute(11)
                        .endTimeInitialHour(10)
                        .endTimeInitialMinute(22)
                        .newInstance(object :OnTimeRangeSelectedListener{
                            override fun onTimeRangeSelected(
                                startHour: Int,
                                startMinute: Int,
                                endHour: Int,
                                endMinute: Int
                            ) {
                                Toast.makeText(context,"开始时间：$startHour:$startMinute, 结束时间：$endHour:$endMinute",Toast.LENGTH_LONG).show()
                            }

                        },is24HourFormat(context) )
                        .show(childFragmentManager,tagBottomSheetTimeRangePicker)
            }}))
    }

    fun SimpleDialog() {
        val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
        builder
            ?.setIcon(com.jube.androiddemo.R.drawable.cancel)
            ?.setTitle("简单对话框")
            ?.setMessage("设置Dialog 显示的内容")
            ?.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            })
            ?.setNegativeButton("Cancle", null)
            ?.create()
            ?.show()
    }

    fun ButtonDialog(){
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage("稍后提醒我")
                setPositiveButton(
                    "ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                    })
                setNegativeButton(
                    "cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            }
            builder.create()
        }
        alertDialog?.show()
    }

    fun BottomSheetDialog(){
        val bottomSheetDialog = context?.let { BottomSheetDialog(it) }
        bottomSheetDialog?.setContentView(R.layout.bottom_sheet_dialog_layout)
        val copy = bottomSheetDialog?.findViewById<LinearLayout>(R.id.copyLinearLayout)
        val share = bottomSheetDialog?.findViewById<LinearLayout>(R.id.shareLinearLayout)
        val upload = bottomSheetDialog?.findViewById<LinearLayout>(R.id.uploadLinearLayout)
        val download = bottomSheetDialog?.findViewById<LinearLayout>(R.id.download)
        val delete = bottomSheetDialog?.findViewById<LinearLayout>(R.id.delete)
        copy!!.setOnClickListener {
            Toast.makeText(
                context,
                "Copy is Clicked ",
                Toast.LENGTH_LONG
            ).show()
            bottomSheetDialog!!.dismiss()
        }

        share!!.setOnClickListener {
            Toast.makeText(
                context,
                "Share is Clicked",
                Toast.LENGTH_LONG
            ).show()
            bottomSheetDialog!!.dismiss()
        }

        upload!!.setOnClickListener {
            Toast.makeText(
                context,
                "Upload is Clicked",
                Toast.LENGTH_LONG
            ).show()
            bottomSheetDialog!!.dismiss()
        }

        download!!.setOnClickListener {
            Toast.makeText(
                context,
                "Download is Clicked",
                Toast.LENGTH_LONG
            ).show()
            bottomSheetDialog!!.dismiss()
        }

        delete!!.setOnClickListener {
            Toast.makeText(
                context,
                "Delete is Clicked",
                Toast.LENGTH_LONG
            ).show()
            bottomSheetDialog!!.dismiss()
        }
        bottomSheetDialog?.show()
    }
}