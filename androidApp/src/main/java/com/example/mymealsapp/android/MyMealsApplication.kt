package com.example.mymealsapp.android

import android.app.Application
import com.example.mymealsapp.android.di.viewModelModule
import com.example.mymealsapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyMealsApplication: Application(){

    override fun onCreate() {
        super.onCreate()
        initKoin{
//            modules(viewModelModule)
            androidContext(this@MyMealsApplication)
        }
    }
}