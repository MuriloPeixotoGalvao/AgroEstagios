package com.example.data

import android.util.Log
import com.example.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClientProvider {
    val isConfigured: Boolean
        get() {
            return try {
                val url = BuildConfig.SUPABASE_URL
                val key = BuildConfig.SUPABASE_ANON_KEY
                url.isNotBlank() &&
                        url.startsWith("http") &&
                        !url.contains("your-supabase-project") &&
                        key.isNotBlank() &&
                        !key.contains("your-supabase-anon-key")
            } catch (e: Exception) {
                false
            }
        }

    val client: SupabaseClient? by lazy {
        if (!isConfigured) {
            null
        } else {
            try {
                createSupabaseClient(
                    supabaseUrl = BuildConfig.SUPABASE_URL,
                    supabaseKey = BuildConfig.SUPABASE_ANON_KEY
                ) {
                    install(Postgrest)
                    install(Auth)
                    install(Storage)
                }
            } catch (e: Exception) {
                Log.e("SupabaseClientProvider", "Error initializing Supabase client", e)
                null
            }
        }
    }
}

