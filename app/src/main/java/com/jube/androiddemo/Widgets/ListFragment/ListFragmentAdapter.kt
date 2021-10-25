package com.jube.androiddemo.Widgets.ListFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.jube.androiddemo.R
import com.jube.androiddemo.Widgets.WidgetsMainFragment.WidgetsMainFragmentAdapter

class ListFragmentAdapter(listFragmentItems: List<ListFragmentItem>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mDataList:List<ListFragmentItem>? = listFragmentItems
    inner class ListFragmentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var mButton: Button? = null
        init {
            mButton = itemView.findViewById(R.id.homepage_btn)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.homepage_item, parent, false)
        return ListFragmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ListFragmentAdapter.ListFragmentViewHolder).mButton?.text = mDataList?.get(position)?.mTitle
        holder.mButton?.setOnClickListener {
            mDataList?.get(position)?.mCallback?.onClick()
        }
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }
}