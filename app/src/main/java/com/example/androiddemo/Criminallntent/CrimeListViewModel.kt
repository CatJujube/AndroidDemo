package com.example.androiddemo.Criminallntent

import androidx.lifecycle.ViewModel
import com.example.androiddemo.Criminallntent.CriminalRoom.CrimeRepository

class CrimeListViewModel: ViewModel() {
//    val crimes = mutableListOf<Crime>()
//    init {
//        for(i in 0 until 100){
//            val crime = Crime()
//            crime.title = "Crime #$i"
//            crime.isSloved = i%2 == 0
//            crimes += crime         //哈哈，kotlin的list居然重载了+号
//        }
//    }

    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()
}