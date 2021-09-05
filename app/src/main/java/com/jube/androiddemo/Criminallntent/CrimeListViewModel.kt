package com.jube.androiddemo.Criminallntent

import androidx.lifecycle.ViewModel
import com.jube.androiddemo.Criminallntent.CriminalRoom.Crime
import com.jube.androiddemo.Criminallntent.CriminalRoom.CrimeRepository

class CrimeListViewModel: ViewModel() {
    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()

    fun addCrime(crime: Crime){
        crimeRepository.addCrime(crime)
    }
}