package com.jube.androiddemo.Widgets.Dialog.BottomSheetTimeRangePicker

interface OnTimeRangeSelectedListener {
    fun onTimeRangeSelected(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int)
}