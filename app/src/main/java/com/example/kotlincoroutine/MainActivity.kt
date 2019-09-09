package com.example.kotlincoroutine

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import kotlinx.coroutines.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            println("time1:" + SystemClock.currentThreadTimeMillis())

            val token = requestToken()
            val post = createPost(token)
            processPost(post)
            println("time2:" + SystemClock.currentThreadTimeMillis())

        }

        println("main:" + Thread.currentThread().name) // 在延迟后打印输出

    }


    private suspend fun requestToken(): String {

        delay(2000)
        println("time requestToken:" + SystemClock.currentThreadTimeMillis())

        println("requestToken:" + Thread.currentThread().name) // 在延迟后打印输出
        return "token"


    }

    private suspend fun createPost(token: String): String {
        delay(1000)
        println("time createPost:" + SystemClock.currentThreadTimeMillis())

        println("createPost:" + Thread.currentThread().name) // 在延迟后打印输出
        return "post$token"
    }

    private fun processPost(post: String) {
        println("time processPost:" + SystemClock.currentThreadTimeMillis())

        println("processPost:$post") // 在延迟后打印输出

    }


    fun test() = runBlocking<Unit> {
        launch {
            // 在后台启动一个新的协程并继续
            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
            println("World!") // 在延迟后打印输出
        }
        println("Hello,") // 协程已在等待时主线程还在继续
//        Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
        delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活

    }


    fun test2() = runBlocking<Unit> {
        repeat(100_000) {
            // 启动大量的协程
            GlobalScope.launch {
                delay(1000L)
                println(it)
            }
        }
//        delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活
    }


    fun test4() {
        thread {
            println("World")
        }
        println("Hello,") // 协程已在等待时主线程还在继续
        Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
    }

    fun test5() = runBlocking {

        val job = GlobalScope.launch {
            // 启动一个新协程并保持对这个作业的引用
            delay(1000L)
            println("World!")
        }
        println("Hello,")
        job.join() // 等待直到子协程执行结束
    }
}
