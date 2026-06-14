package com.example.coffeebliss.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val memberId: Int,
    val amount: Double,
    val pointEarned: Int,   // positive = earned (purchase), negative = used (redeem)
    val date: String,
    val type: String = "purchase", // "purchase" | "redeem"
    val note: String = ""          // reward name for redemptions
)
