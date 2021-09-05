package com.jube.androiddemo.Criminallntent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jube.androiddemo.R
import java.util.*

class CrimeActivity : AppCompatActivity() , CrimeListFragment.Callbacks {

    companion object{
        const val TAG = "CrimeActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if(currentFragment == null){
            val fragment = CrimeListFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }

    override fun onCrimeSelected(crimeId: UUID) {
        Log.i(TAG,"CrimeActivity.onCrimeSelected:$crimeId")
        val fragment = CrimeFragment.newInstance(crimeId)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }
}