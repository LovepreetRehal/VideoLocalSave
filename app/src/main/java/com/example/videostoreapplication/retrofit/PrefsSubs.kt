package com.app.casino.retrofit

import android.content.Context
import android.content.SharedPreferences
import com.example.videostoreapplication.R

class PrefsSubs(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(context.getString(
        R.string.app_name), Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun setInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    fun setString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun setPremium(value: Int) {
        editor.putInt("Premium", value)
        editor.apply()
    }

    fun setBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String, def: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, def)
    }

    fun getInt(key: String, def: Int): Int {
        return sharedPreferences.getInt(key, def)
    }

    fun getString(key: String, def: String): String? {
        return sharedPreferences.getString(key, def)
    }




    fun getPremium(): Int {
        return sharedPreferences.getInt("Premium", 0)
    }

    fun isRemoveAd(): Boolean {
        return getBoolean("isRemoveAd", false)
    }

    fun canDownload(): Boolean {
        return getBoolean("canDownload", false)
    }

    fun setIsRemoveAd(value: Boolean) {
        editor.putBoolean("isRemoveAd", value)
        editor.apply()
    }

    fun setCanDownload(value: Boolean) {
        editor.putBoolean("canDownload", value)
        editor.apply()
    }
}
