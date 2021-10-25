package com.jube.androiddemo.Widgets
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jube.androiddemo.R
import com.jube.androiddemo.Widgets.WidgetsMainFragment.WidgetsMainFragment

class WidgetsMainActivity : AppCompatActivity() {
    companion object{
        const val TAG = "WidgetsMain_Log"
    }
    private val mMainFragment:WidgetsMainFragment = WidgetsMainFragment.newInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG,"onCreate")
        setContentView(R.layout.activity_widgets_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, mMainFragment).commit()
    }
}