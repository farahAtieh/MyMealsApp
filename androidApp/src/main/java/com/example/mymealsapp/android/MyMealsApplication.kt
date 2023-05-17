package com.example.mymealsapp.android

import android.app.Application
import com.example.mymealsapp.di.initKoin

class MyMealsApplication: Application(){

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}