package com.jube.androiddemo.Widgets.WidgetsMainFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jube.androiddemo.R

class WidgetsMainFragment : Fragment() {
    private var mView:View?=null
    private var mDataList:MutableList<WidgetsMainFragmentItem>? = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG,"onCreate")
        initData()
//        val toolBar: Toolbar? = activity?.findViewById(R.id.my_toolbar)
//        (activity as AppCompatActivity).setSupportActionBar(toolBar)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG,"onCreateView")
        mView = inflater.inflate(R.layout.fragment_widgets_main, container, false)
        if(!mDataList.isNullOrEmpty()) {
            val recycleContainer: RecyclerView? = mView?.findViewById(R.id.listContainer)
            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            recycleContainer?.layoutManager = layoutManager
            val adapter = mDataList?.let { WidgetsMainFragmentAdapter(it) }
            recycleContainer?.adapter = adapter
        }else{
            Log.i(TAG,"data list is empty!")
        }
        return mView
    }

    companion object {
        const val TAG = "WidgetsMain_Log"

        @JvmStatic
        fun newInstance() = WidgetsMainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    fun initData(){
        Log.i(TAG,"initData")
        mDataList?.add(WidgetsMainFragmentItem("Dialog",object:WidgetsMainFragmentItem.IClickCallback{
            override fun click() {
                Log.i(TAG," Dialog is clicked")
                mView?.let { Navigation.findNavController(it).navigate(R.id.dialogFragment) }
            }
        }))

        mDataList?.add(WidgetsMainFragmentItem("Layout",object:WidgetsMainFragmentItem.IClickCallback{
            override fun click() {
                Log.i(TAG," Dialog is clicked")
                mView?.let { Navigation.findNavController(it).navigate(R.id.layoutMainFragment) }
            }
        }))
    }
}

