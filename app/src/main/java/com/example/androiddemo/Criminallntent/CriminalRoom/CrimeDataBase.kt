package com.example.androiddemo.Criminallntent.CriminalRoom

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Crime::class], version = 1)
@TypeConverters(CrimeTypeConverts::class)
abstract class CrimeDataBase :RoomDatabase(){
    abstract fun crimeDao():CrimeDao
}