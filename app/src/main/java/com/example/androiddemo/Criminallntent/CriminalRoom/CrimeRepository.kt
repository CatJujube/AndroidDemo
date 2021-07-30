package com.example.androiddemo.Criminallntent.CriminalRoom

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

//使用仓库模式访问数据库
//这里是使用kotlin实现的单例
private const val DATABASE_NAME = "crime_database"


class CrimeRepository private constructor(context: Context){
    private val dataBase:CrimeDataBase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDataBase::class.java,
        DATABASE_NAME
    ).build()

    private val crimeDao = dataBase.crimeDao()
    private val executor = Executors.newSingleThreadExecutor()

    companion object{
        private var INSTANCE:CrimeRepository? = null

        fun initalize(context: Context){
            if(INSTANCE == null){
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get():CrimeRepository{
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized!")
        }
    }

    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()

    fun getCrime(id:UUID):LiveData<Crime?> = crimeDao.getCrime(id)

    fun updateCrime(crime: Crime){
        executor.execute {
            crimeDao.updateCrime(crime)
        }
    }

    fun addCrime(crime: Crime){
        executor.execute {
            crimeDao.addCrime(crime)
        }
    }
}