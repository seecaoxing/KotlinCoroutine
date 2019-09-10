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
        main7()
    }

    private fun main7() {
        GlobalScope.launch(Dispatchers.Unconfined) {
            println("协程 开始执行，时间:  ${System.currentTimeMillis()}")
            var token = GlobalScope.async(Dispatchers.Unconfined) {
                return@async getToken()
            }.await()

            var response = GlobalScope.async(Dispatchers.Unconfined) {
                return@async getResponse(token)
            }.await()

            setText(response)
        }
    }

    private suspend fun getToken(): String {
        delay(300)
        println("getToken 开始执行，时间:  ${System.currentTimeMillis()}")
        return "ask"
    }

    private suspend fun getResponse(token: String): String {
        delay(100)
        println("getResponse 开始执行$token，时间:  ${System.currentTimeMillis()}")
        return "response"
    }

    private fun setText(response: String) {
        println("setText 开始执行$response ，时间:  ${System.currentTimeMillis()}")
    }

    private fun main6() {
        // 运行时
        GlobalScope.launch(Dispatchers.Main) {
            println("协程 开始执行，时间:  ${System.currentTimeMillis()}")
            val token = getToken()
            val response = getResponse(token)
            setText(response)
        }
    }


    private fun main5() {

        GlobalScope.launch(Dispatchers.Unconfined) {
            for (i in 1..3) {
                println("协程任务打印第$i 次，时间: ${System.currentTimeMillis()}")
            }
        }

        for (i in 1..3) {
            println("主线程打印第$i 次，时间:  ${System.currentTimeMillis()}")
        }
    }

    private fun main4() {
        println("main ${System.currentTimeMillis()}")

        runBlocking {
            // 阻塞1s
            delay(1000L)
            println("This is a coroutines ${System.currentTimeMillis()}")
        }
        // 阻塞2s
        Thread.sleep(2000L)
        println("main end ${System.currentTimeMillis()}")
    }


    private fun main3() {

        GlobalScope.launch(Dispatchers.Unconfined) {
            val deferred = GlobalScope.async {
                delay(1000L)
                Log.d("AA", "This is async ")
                return@async "taonce"
            }
            Log.d("AA", "协程 other start")
            val result = deferred.await()
            Log.d("AA", "async result is $result")
            Log.d("AA", "协程 other end ")
        }
        Log.d("AA", "主线程位于协程之后的代码执行，时间:  ${System.currentTimeMillis()}")
    }

    private fun main2() {

        Log.d("AA", "协程初始化开始，时间: " + System.currentTimeMillis())

        GlobalScope.launch(context = Dispatchers.Unconfined) {
            Log.d("AA", "launch:" + Thread.currentThread().name) // 在延迟后打印输出

            Log.d("AA", "协程初始化完成，时间: " + System.currentTimeMillis())

            for (i in 1..3) {
                Log.d("AA", "协程任务1打印第$i 次，时间: " + System.currentTimeMillis())
            }

            delay(500)

            for (i in 1..3) {
                Log.d("AA", "协程任务2打印第$i 次，时间: " + System.currentTimeMillis())
            }

        }

        Log.d("AA", "主线程 sleep ，时间: " + System.currentTimeMillis())

        Thread.sleep(1000)

        Log.d("AA", "主线程运行，时间: " + System.currentTimeMillis())

        for (i in 1..3) {
            Log.d("AA", "主线程打印第$i 次，时间: " + System.currentTimeMillis())
        }

    }


    private fun main1() {

        GlobalScope.launch(Dispatchers.Unconfined) {
            println("launch:" + Thread.currentThread().name) // 在延迟后打印输出

            println("time1:" + System.currentTimeMillis())

            val token = requestToken()
            val post = createPost(token)
            processPost(post)

            println("time2:" + System.currentTimeMillis())
        }
        println("main:" + Thread.currentThread().name)


    }


    private suspend fun requestToken(): String {
        delay(500)
        println("time requestToken:" + System.currentTimeMillis())
        println("requestToken:" + Thread.currentThread().name) // 在延迟后打印输出
        return "token"
    }

    private suspend fun createPost(token: String): String {
        println("time createPost:" + System.currentTimeMillis())
        println("createPost:" + Thread.currentThread().name) // 在延迟后打印输出
        return "post$token"
    }

    private suspend fun processPost(post: String) {
        println("time processPost:" + System.currentTimeMillis())
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
