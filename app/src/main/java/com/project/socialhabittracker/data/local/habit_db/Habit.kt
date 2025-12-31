package com.project.socialhabittracker.data.local.habit_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "note")
    val note: String = "Chains of habit are too light to be felt until they are too heavy to be broken.",

    @ColumnInfo(name = "unit")
    val unit: String = "Units",

    @ColumnInfo(name = "target_count")
    val targetCount: String = "0",

    @ColumnInfo(name = "type")
    val type: String = "Yes or No" // "Yes or No" or "measurable"
)