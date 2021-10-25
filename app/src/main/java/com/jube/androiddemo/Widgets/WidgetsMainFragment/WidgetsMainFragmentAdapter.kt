package com.jube.androiddemo.Widgets.WidgetsMainFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.jube.androiddemo.R

class WidgetsMainFragmentAdapter(widgetMainFragmentItems:List<WidgetsMainFragmentItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class WidgetMainFragmentViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var mButton:Button? = null
        init {
            mButton = itemView.findViewById(R.id.homepage_btn)
        }
    }
    private var mDataList:List<WidgetsMainFragmentItem>? = widgetMainFragmentItems
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.homepage_item, parent, false)
        return WidgetMainFragmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as WidgetMainFragmentViewHolder).mButton?.text = mDataList?.get(position)?.mTitle
        holder.mButton?.setOnClickListener {
            mDataList?.get(position)?.mCallback?.click()
        }
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }
}