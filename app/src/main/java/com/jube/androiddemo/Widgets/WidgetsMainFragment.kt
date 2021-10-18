package com.jube.androiddemo.Widgets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jube.androiddemo.R

class WidgetsMainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_widgets_main, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            WidgetsMainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}