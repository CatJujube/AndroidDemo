package com.jube.androiddemo.Widgets.Layouts

import android.os.Bundle
import androidx.navigation.Navigation
import com.jube.androiddemo.R
import com.jube.androiddemo.Widgets.ListFragment.ListFragment
import com.jube.androiddemo.Widgets.ListFragment.ListFragmentItem

class LayoutMainFragment:ListFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData(){
        clear()
        add(ListFragmentItem("Relativelayout",object :ListFragmentItem.IListFragmentCallback{
            override fun onClick() {
                view?.let { Navigation.findNavController(it).navigate(R.id.relativelayoutFragment) }
            }
        }))
        add(ListFragmentItem("FrameLayout",object :ListFragmentItem.IListFragmentCallback{
            override fun onClick() {
                view?.let { Navigation.findNavController(it).navigate(R.id.frameLayoutFragment) }
            }
        }))
    }
}