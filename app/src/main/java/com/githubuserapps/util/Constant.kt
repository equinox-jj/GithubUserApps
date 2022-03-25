package com.githubuserapps.util

import com.githubuserapps.BuildConfig

class Constant {
    companion object {
        // APPS CONSTANT
        const val BASE_URL = "https://api.github.com/"
        const val TOKEN = BuildConfig.TOKEN // INPUT YOUR GITHUB API TOKEN HERE

        // DATA STORE CONSTANT
        const val DATASTORE_PREFERENCE = "data_store_preference"
        const val THEME_MODE_KEY = "theme_mode"
    }
}