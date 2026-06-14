package com.example.coffeebliss.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean = getMemberId() != -1

    fun getMemberId(): Int = prefs.getInt(KEY_MEMBER_ID, -1)

    fun saveSession(memberId: Int) {
        prefs.edit().putInt(KEY_MEMBER_ID, memberId).apply()
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME    = "coffee_bliss_session"
        private const val KEY_MEMBER_ID = "member_id"
    }
}
