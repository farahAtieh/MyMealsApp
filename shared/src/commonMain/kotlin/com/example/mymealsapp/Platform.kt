package com.example.mymealsapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform