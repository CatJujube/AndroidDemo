package com.jube.androiddemo.Widgets.FragmentNav

import android.os.Bundle
import com.jube.androiddemo.R
import com.jube.androiddemo.Widgets.ListFragment.ListFragment
import com.jube.androiddemo.Widgets.ListFragment.ListFragmentItem

class ChildFragment1:ListFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        add(ListFragmentItem("child fragment 1",object :ListFragmentItem.IListFragmentCallback{
            override fun onClick() {
                parentFragmentManager.beginTransaction().replace(R.id.containerLayout,ChildFragment2()).commit()
            }
        }))
    }
}