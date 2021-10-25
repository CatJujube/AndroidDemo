package com.jube.androiddemo.Widgets.Layouts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jube.androiddemo.R

class RelativelayoutFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_relativelayout, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RelativelayoutFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}