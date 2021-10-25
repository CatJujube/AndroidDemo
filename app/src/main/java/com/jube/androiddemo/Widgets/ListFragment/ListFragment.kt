package com.jube.androiddemo.Widgets.ListFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jube.androiddemo.R

open class ListFragment:Fragment() {
    private var mView:View?=null
    private var mDataList:MutableList<ListFragmentItem>? = mutableListOf()
    private var mAdapter:ListFragmentAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_list_fragment, container, false)
        val recycleContainer: RecyclerView? = mView?.findViewById(R.id.listContainer2)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycleContainer?.layoutManager = layoutManager
        mAdapter = mDataList?.let { ListFragmentAdapter(it) }
        recycleContainer?.adapter = mAdapter
        return mView
    }

    @SuppressLint("NotifyDataSetChanged")
    fun add(item:ListFragmentItem){
        mDataList?.add(item)
        mAdapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeAt(idx:Int){
        mDataList?.removeAt(idx)
        mAdapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initData(listItems:List<ListFragmentItem>){
        if (listItems.isEmpty()){
            mDataList?.clear()
            return
        }
        mDataList = listItems as MutableList<ListFragmentItem>?
        mAdapter?.notifyDataSetChanged()
    }

    fun clear(){
        mDataList?.clear()
        mAdapter?.notifyDataSetChanged()
    }
}