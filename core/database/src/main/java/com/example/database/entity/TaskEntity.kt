package com.example.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity (
    @PrimaryKey val id: String,
    val dayId: String,
    val title: String,
    val time: Long? = null,
    val description: String? = null,
    val isCompleted: Boolean = false
)