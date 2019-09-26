package com.example.kotlincoroutine

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

    }

    fun main28() {
        suspend fun getA() {
            kotlinx.coroutines.delay(2000)
            println("getA...." + Thread.currentThread().name)
        }

        GlobalScope.launch(Dispatchers.Unconfined) {
            getA()

        }
    }
}
