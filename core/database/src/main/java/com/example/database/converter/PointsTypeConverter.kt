package com.example.database.converter

import androidx.room.TypeConverter
import com.example.database.entity.PointEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PointsTypeConverter {
    private val gson = Gson()
    private val type = object : TypeToken<List<PointEntity>>() {}.type

    @TypeConverter
    fun fromJson(json: String): List<PointEntity> =
        gson.fromJson(json, type) ?: emptyList()

    @TypeConverter
    fun toJson(points: List<PointEntity>): String =
        gson.toJson(points)
}