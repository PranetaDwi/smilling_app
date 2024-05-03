package com.example.smilling_app

import android.content.Context
import android.content.SharedPreferences

class PrefManager private constructor(context: Context){

    private val sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_FILENAME = "AuthAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val USER_ID = "userId"
        @Volatile
        private var instance: PrefManager? = null
        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILENAME,
            Context.MODE_PRIVATE)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveUserId (userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString(USER_ID, userId)
        editor.apply()
    }

    fun getUserId(): String {
        return sharedPreferences.getString(USER_ID, "") ?: ""
    }
    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }




}