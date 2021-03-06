package com.example.core_data

import android.app.Application
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.core_data.BuildConfig.BASE_URL
import com.example.core_data.api.ApiExecutor
import com.example.core_data.api.apiClient
import com.example.core_data.api.httpClient
import com.example.core_data.api.service.AnswerService
import com.example.core_data.api.service.AuthService
import com.example.core_data.api.service.GuruService
import com.example.core_data.api.service.SiswaService
import com.example.core_data.api.service.UserService
import com.example.core_data.repository.AnswerRepository
import com.example.core_data.api.service.*
import com.example.core_data.repository.AuthRepository
import com.example.core_data.repository.GuruRepository
import com.example.core_data.repository.NilaiSiswaRepository
import com.example.core_data.repository.SiswaRepository
import com.example.core_data.repository.LastResultRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.gotev.cookiestore.SharedPreferencesCookieStore
import net.gotev.cookiestore.okhttp.JavaNetCookieJar
import okhttp3.CookieJar
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.Koin
import org.koin.dsl.module
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.CookieStore

@Suppress("MaxLineLength")
val Application.dataModule
    get() = module {
        single<CookieStore> { SharedPreferencesCookieStore(applicationContext, "aplikasi_pakar") }
        single<CookieHandler> { CookieManager(get(), CookiePolicy.ACCEPT_ALL) }
        single<CookieJar> { JavaNetCookieJar(get()) }
        single {
            httpClient(
                TIMEOUT,
                HttpLoggingInterceptor.Level.BODY,
                get(),
                ChuckerInterceptor.Builder(get()).build()
            )
        }

        single { Moshi.Builder().build() }
        single { ApiExecutor(get()) }

        single { apiClient<AuthService>(BASE_URL, get()) }
        single { apiClient<UserService>(BASE_URL, get()) }
        single { apiClient<GuruService>(BASE_URL, get()) }
        single { apiClient<SiswaService>(BASE_URL, get()) }
        single { apiClient<AnswerService>(BASE_URL, get()) }
        single { apiClient<NilaiSiswaService>(BASE_URL, get()) }

        single {
            Room.databaseBuilder(get(), CoreDatabase::class.java, "aplikasi_pakar_db")
                .fallbackToDestructiveMigration()
                .build()
        }

        single { get<CoreDatabase>().userDao() }
        single { get<CoreDatabase>().lastResultDao() }
        single { get<CoreDatabase>().siswaDao() }
        single { get<CoreDatabase>().guruDao() }

        single { AuthRepository(get(), get(), get(), get(), get(), get()) }
        single { GuruRepository(get(), get(), get(), get()) }
        single { SiswaRepository(get(), get(), get(), get()) }
        single { AnswerRepository(get(), get(), get(), get()) }
        single { LastResultRepository(get(), get(), get()) }

        single { NilaiSiswaRepository(get(), get(), get()) }
    }

private const val TIMEOUT = 30L

/**
 * Clear local database
 */
suspend fun Koin.clearAppData() {
    withContext(Dispatchers.IO) {
        getOrNull<CoreDatabase>()?.clearAllTables()
    }
}

fun CookieHandler.removeAll() {
    (this as CookieManager).cookieStore.removeAll()
}