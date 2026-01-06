package com.example.alp_vp.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.alp_vp.data.dto.auth.ProfileData

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "game_tracker_session"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_DISPLAY_NAME = "display_name"
        private const val KEY_POINTS = "points"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun saveAuthData(token: String, profile: ProfileData) {
        prefs.edit().apply {
            putString(KEY_TOKEN, token)
            putInt(KEY_USER_ID, profile.id)
            putString(KEY_USERNAME, profile.username)
            putString(KEY_EMAIL, profile.email)
            putString(KEY_DISPLAY_NAME, profile.displayName)
            putInt(KEY_POINTS, profile.points)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)

    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    fun getDisplayName(): String? = prefs.getString(KEY_DISPLAY_NAME, null)

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun logout() {
        prefs.edit().clear().apply()
    }
}

