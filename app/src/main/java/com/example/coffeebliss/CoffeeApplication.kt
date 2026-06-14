package com.example.coffeebliss

import android.app.Application
import com.example.coffeebliss.data.AppDatabase
import com.example.coffeebliss.data.CoffeeRepository
import com.example.coffeebliss.data.SessionManager

class CoffeeApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { CoffeeRepository(database.memberDao(), database.transactionDao()) }
    val sessionManager by lazy { SessionManager(this) }
}
