package com.example.domain.model

data class Task(
    val id: String,
    val time: Long,
    val title: String,
    val note: String,
    val isCompleted: Boolean = false
)