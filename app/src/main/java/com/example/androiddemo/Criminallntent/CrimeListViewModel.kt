package com.example.androiddemo.Criminallntent

import androidx.lifecycle.ViewModel
import com.example.androiddemo.Criminallntent.CriminalRoom.Crime
import com.example.androiddemo.Criminallntent.CriminalRoom.CrimeRepository

class CrimeListViewModel: ViewModel() {
    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()

    fun addCrime(crime: Crime){
        crimeRepository.addCrime(crime)
    }
}