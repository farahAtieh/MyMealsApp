package com.example.mymealsapp.di

import com.example.mymealsapp.db.MealsDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.dsl.module

actual val platformModule = module {
    single<SqlDriver>{
        NativeSqliteDriver(MealsDatabase.Schema, "mymealsapp.db")
    }
}