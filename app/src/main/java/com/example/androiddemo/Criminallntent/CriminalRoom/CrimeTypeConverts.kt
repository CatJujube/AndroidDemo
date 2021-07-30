package com.example.androiddemo.Criminallntent.CriminalRoom

import androidx.room.TypeConverter
import java.util.*

class CrimeTypeConverts {
    @TypeConverter
    fun fromDate(date: Date?):Long?{
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSenceEpoch:Long?):Date?{
        return millisSenceEpoch?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun toUUID(uuid:String?):UUID?{
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?):String{
        return uuid.toString()
    }
}