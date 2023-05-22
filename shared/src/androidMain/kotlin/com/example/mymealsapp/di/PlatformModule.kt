package com.example.mymealsapp.di

import com.example.mymealsapp.db.MealsDatabase

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.dsl.module

actual val platformModule = module {
    single<SqlDriver>{
        AndroidSqliteDriver(
            MealsDatabase.Schema,
            get(),
            "mymealsapp.db"
        )
    }
}