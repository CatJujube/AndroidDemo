package com.example.androiddemo.Criminallntent.CriminalRoom

import android.app.Application

class CriminalIntentApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initalize(this)
    }
}