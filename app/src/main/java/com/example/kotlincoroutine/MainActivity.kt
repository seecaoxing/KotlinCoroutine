package com.example.kotlincoroutine

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    val gitHubServiceApi by lazy {
        val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())//添加对 Deferred 的支持
            .build()
        retrofit.create(GitHubServiceApi::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main14()

    }

    private fun main14() {

        GlobalScope.launch(Dispatchers.Unconfined) {

            System.out.println("Thread ${Thread.currentThread().name}")

            GlobalScope.launch {
                delay(1000)
                System.out.println("Thread ${Thread.currentThread().name}")
            }

            System.out.println("end")
        }
        System.out.println("main")

    }


    private fun main13() {

        GlobalScope.launch(Dispatchers.Unconfined) {

            System.out.println("Thread ${Thread.currentThread().name}")

            var result = GlobalScope.async {
                delay(1000)
                System.out.println("Thread ${Thread.currentThread().name}")
            }

            System.out.println("result ${result.await()}")

            System.out.println("end")
        }

        System.out.println("main")

    }

    private fun main12() {

        GlobalScope.launch(Dispatchers.Main) {
            System.out.println("1-1")

            delay(2000)
            System.out.println("1-2")
        }

        GlobalScope.launch(Dispatchers.Main) {
            System.out.println("2-1")

            delay(3000)
            System.out.println("2-2")
        }

        System.out.println("main")


    }

    private fun main11() {

        suspend fun getToken(): String {
            delay(100)
            println("getToken 开始执行，时间:  ${System.currentTimeMillis()}")
            return "ask"
        }

        GlobalScope.launch(Dispatchers.Main) {
            System.out.println("Thread " + Thread.currentThread().name)
            var result = GlobalScope.async {
                System.out.println("Thread " + Thread.currentThread().name)
                getToken()
            }
            System.out.println("result ${result.await()}")
            System.out.println("end")

        }
    }

    private fun main10() {
        gitHubServiceApi.getUser("bennyhuo").enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                System.out.println("Thread " + Thread.currentThread().name)
                Toast.makeText(this@MainActivity, "onFailure", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                System.out.println("Thread " + Thread.currentThread().name)
                Toast.makeText(this@MainActivity, "onResponse", Toast.LENGTH_SHORT).show()
            }
        })



        GlobalScope.launch(Dispatchers.Main) {
            try {
                System.out.println(Thread.currentThread().name)
                System.out.println(gitHubServiceApi.getUser2("bennyhuo").await())
            } catch (e: Exception) {
                System.out.println(Thread.currentThread().name)
                System.out.println(e)
            }

        }
    }

    private fun main9() {

        suspend fun getToken(): String {
            delay(200)
            println("getToken  开始执行，时间:  ${System.currentTimeMillis()}" + "，线程：" + Thread.currentThread().name)
            return "ask"
        }

        suspend fun getResponse(token: String): String {
            delay(200)
            println("getResponse  开始执行$token，2时间:  ${System.currentTimeMillis()}" + "，线程：" + Thread.currentThread().name)
            return "response"
        }

        fun setText(response: String) {
            println("setText 开始执行$response ，时间:  ${System.currentTimeMillis()}" + "，线程：" + Thread.currentThread().name)
        }

        // 协程任务
        var job = GlobalScope.launch(Dispatchers.IO) {
            println("协程测试 开始执行，线程：${Thread.currentThread().name}")
            var token = GlobalScope.async(Dispatchers.IO) {
                return@async getToken()
            }.await()

            var response = GlobalScope.async(Dispatchers.IO) {
                return@async getResponse(token)
            }.await()

            setText(response)
        }

        // 取消协程
        job?.cancel()

        println("btn_right 结束协程")
    }

    private fun main8() {

        suspend fun getToken(): String {
            println("getToken 1 开始执行，时间:  ${System.currentTimeMillis()}" + "，线程：" + Thread.currentThread().name)
            delay(200)
            println("getToken 2 开始执行，时间:  ${System.currentTimeMillis()}" + "，线程：" + Thread.currentThread().name)
            return "ask"
        }

        suspend fun getResponse(token: String): String {
            println("getResponse 1 开始执行$token，时间:  ${System.currentTimeMillis()}" + "，线程：" + Thread.currentThread().name)
            delay(200)
            println("getResponse 2 开始执行$token，2时间:  ${System.currentTimeMillis()}" + "，线程：" + Thread.currentThread().name)
            return "response"
        }

        fun setText(response: String) {
            println("setText 开始执行$response ，时间:  ${System.currentTimeMillis()}" + "，线程：" + Thread.currentThread().name)
        }

        GlobalScope.launch(Dispatchers.IO) {
            println("协程测试 开始执行，线程：${Thread.currentThread().name}")
            var token = GlobalScope.async(Dispatchers.Unconfined) {
                return@async getToken()
            }.await()

            GlobalScope.launch {
                println("测试 会不会阻塞其他，线程：${Thread.currentThread().name}")
            }
            var response = GlobalScope.async(Dispatchers.Unconfined) {
                return@async getResponse(token)
            }.await()
            setText(response)
        }
        println("主线程协程后面代码执行，线程：${Thread.currentThread().name}")

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
        delay(100)
        println("getToken 开始执行，时间:  ${System.currentTimeMillis()}")
        return "ask"
    }

    private suspend fun getResponse(token: String): String {
        delay(200)
        println("getResponse 开始执行$token，时间:  ${System.currentTimeMillis()}")
        return "response"
    }

    private fun setText(response: String) {
        println("setText 开始执行$response ，时间:  ${System.currentTimeMillis()}")
    }

    private fun main6() {
        // 运行时
        GlobalScope.launch(Dispatchers.Unconfined) {
            println("协程 开始执行，时间:  ${System.currentTimeMillis()}")
            val token = getToken()
            val response = getResponse(token)
            setText(response)
        }
        for (i in 1..10) {
            println("主线程打印第$i 次，时间:  ${System.currentTimeMillis()}")
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
            delay(1000L)
            println("This is a coroutines ${System.currentTimeMillis()}")
        }
        Thread.sleep(500L)
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
