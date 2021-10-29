package com.jube.androiddemo.Widgets.FragmentNav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jube.androiddemo.R
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class FragmentNavMainActivity : AppCompatActivity() {
    //记录当前Fragment
    private var nowFragment: Fragment? = null
    //其他的Fragment
    private var newsFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_nav_main)
        initFragment(savedInstanceState)
        if(newsFragment == null){
            newsFragment = ChildFragment1()
        }
        switchContent(nowFragment, newsFragment!!)
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        //避免横竖屏时出现页面叠加，判断activity是否重新加载
        if (savedInstanceState == null) {
            val fm: FragmentManager = supportFragmentManager
            val ft: FragmentTransaction = fm.beginTransaction()
            if (newsFragment == null) {
                newsFragment = ChildFragment1()
            }
            nowFragment = newsFragment
            nowFragment?.let { ft.replace(R.id.containerLayout, it).commitAllowingStateLoss() }
        }
    }

    fun switchContent(from: Fragment?, to: Fragment) {
        if (nowFragment !== to) {
            nowFragment = to
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            //检测去往的Fragment是否被添加
            if (!to.isAdded) {
                //如果没有添加，就隐藏当前Fragment，添加下一个Fragment
                ft.hide(from!!).add(R.id.containerLayout, to).commitAllowingStateLoss()
            } else {
                //如果已经被添加，就隐藏当前Fragment，直接显示下一个Fragement
                ft.hide(from!!).show(to).commitAllowingStateLoss()
            }
        }
    }
}