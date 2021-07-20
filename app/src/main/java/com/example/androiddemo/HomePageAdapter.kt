package com.example.androiddemo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class HomePageAdapter(homePageItemList: List<HomePageItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mDataList:List<HomePageItem>? = null

    init {
        mDataList = homePageItemList
    }

    internal class HomePageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mItemButton:Button? = null

        init {
            mItemButton = itemView.findViewById(R.id.homepage_btn)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.homepage_item, parent, false)
        val holder = HomePageViewHolder(view)
        holder.mItemButton?.setOnClickListener { v ->
            Log.i(MainActivity.TAG,"mItemButton is clicked!")
            mDataList?.get(holder.adapterPosition)?.mCallback?.onClick()
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HomePageViewHolder).mItemButton?.text = mDataList?.get(position)?.mTitle
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

}