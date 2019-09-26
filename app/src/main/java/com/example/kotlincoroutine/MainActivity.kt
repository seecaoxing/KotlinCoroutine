package com.example.kotlincoroutine

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import okhttp3.Dispatcher
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
        main35()
    }


    fun main36() {
        GlobalScope.launch(Dispatchers.IO, CoroutineStart.UNDISPATCHED) {

            println(Thread.currentThread().name)
            kotlinx.coroutines.delay(1000)
            println(Thread.currentThread().name)

        }
    }


    fun main35() {

        suspend fun suspendGetResult(): String {
            return withContext(Dispatchers.IO) {
                System.out.println("Thread name: " + Thread.currentThread().name + "  开始工作")
                delay(1000)
                return@withContext "result"
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            var result = suspendGetResult()
            System.out.println("Thread name: " + Thread.currentThread().name + "  结果: " + result)
        }

        GlobalScope.launch(Dispatchers.Main) {
            System.out.println("Thread name: " + Thread.currentThread().name + "  另一个协程 ")
        }

    }


    fun main34() {


        GlobalScope.launch(Dispatchers.Main) {
            System.out.println("start ")

            var aa = GlobalScope.async(Dispatchers.Main) {
                kotlinx.coroutines.delay(1000)
                return@async "aaaaa"
            }

            var bb = GlobalScope.async(Dispatchers.Main) {
                kotlinx.coroutines.delay(1000)
                return@async "bbbbb"
            }

            var cc = aa.await() + bb.await()

            System.out.println(cc)

        }


    }


    fun main33() {
        GlobalScope.launch(Dispatchers.Main) {

            var token = GlobalScope.async(Dispatchers.IO) {
                return@async getToken()
            }

            var response = GlobalScope.async(Dispatchers.IO) {
                //                return@async getResponse()
            }

//            show(response)

        }
    }

    fun main32() {

        GlobalScope.launch(Dispatchers.Main) {

            withContext(Dispatchers.IO) {
                //...
            }
            withContext(Dispatchers.Default) {
                //...
            }

        }


    }


    fun main31() {

        suspend fun suspendGetResult(): String {
            return withContext(Dispatchers.IO) {
                System.out.println("Thread name: " + Thread.currentThread().name + "挂起 开始")

                delay(1000)
                System.out.println("Thread name: " + Thread.currentThread().name + "挂起 结束")

                return@withContext "result"
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            var result = suspendGetResult()
            System.out.println("Thread name: " + Thread.currentThread().name + "  结果: " + result)
        }

    }


    fun main30() {

        fun cpuReaper() {
            var num: Int = 0
            var start: Long = System.currentTimeMillis() / 1000
            while (true) {
                num += 1
                println("cpuReaper...." + Thread.currentThread().name)
                if (num == Integer.MAX_VALUE) {
                    System.out.println("reset")
                    num = 0
                }
                if ((System.currentTimeMillis() / 1000) - start > 1000) {
                    return
                }
            }
        }

        suspend fun getA() {
            withContext(Dispatchers.Default) {
                cpuReaper()
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            getA()
            println("aaaaaaaaaaaa")
        }

        println("bbbbbbbbbbbbbb")

    }


    fun main29() {


        suspend fun getA() {
            kotlinx.coroutines.delay(2000)
            println("getA...." + Thread.currentThread().name)
        }

        GlobalScope.launch(Dispatchers.Default) {
            getA()
        }

        GlobalScope.launch(Dispatchers.Default) {

        }
        println("main...." + Thread.currentThread().name)


    }


    fun main28() {
        suspend fun getA() {
            kotlinx.coroutines.delay(2000)
            println("getA...." + Thread.currentThread().name)
        }

        suspend fun getB() {
            kotlinx.coroutines.delay(1000)
            println("getB...." + Thread.currentThread().name)
        }

        GlobalScope.launch(Dispatchers.Unconfined) {
            getA()

        }

        GlobalScope.launch(Dispatchers.Unconfined) {
            getB()

        }

        println("main...." + Thread.currentThread().name)

    }


    suspend fun requestDataAsync(): String { // 请注意方法前多了一个suspend关键字
        return GlobalScope.async {
            // 先不要管这个async方法, 后面解释
            // do something need lots of times.
            // ...
            return@async "aaadd"  // return data, lambda里的return要省略
        }.await()
    }

    fun main27() {
        requestDataAsync {
            println("data is $it")
        }
        Thread.sleep(10000L)  // 这个sleep只是为了保活进程
    }

    fun requestDataAsync(callback: (String) -> Unit) {
        Thread() {
            Thread.sleep(1111)
            callback("aaaaa")
        }.start()
    }


    fun main26() {

        System.out.println("main start")

        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            System.out.println("协程1 执行完毕")
        }

        GlobalScope.launch(Dispatchers.Main) {
            System.out.println("协程2 执行完毕")
        }

    }


    fun main25() {
        GlobalScope.launch(Dispatchers.Main) {
            repeat(3) {
                System.out.println("协程一")
                yield()
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            repeat(3) {
                System.out.println("协程二")
                yield()
            }
        }
    }


    fun main24() {
        System.out.println("main start")//1

        var job = GlobalScope.launch {
            var result = GlobalScope.async {
                System.out.println("11111")//2
                kotlinx.coroutines.delay(1000)
                System.out.println("22222")//5
                return@async "aaaa"
            }
            System.out.println("3333")//3

            Thread.sleep(111)

            yield()
            System.out.println(result.await())//6
            System.out.println("4444")//4

        }
        System.out.println("main end")//1

    }


    fun main23() {

        System.out.println("main start")

        GlobalScope.launch(Dispatchers.Unconfined) {
            System.out.println("GlobalScope 1 start Thread name: ${Thread.currentThread().name},${System.currentTimeMillis()}")
            kotlinx.coroutines.delay(2)
            System.out.println("GlobalScope 1 end Thread name: ${Thread.currentThread().name},${System.currentTimeMillis()}")
        }

        GlobalScope.launch(Dispatchers.Unconfined) {
            for (i in 1..100) {
                System.out.println("第${i}次,Thread name: ${Thread.currentThread().name},${System.currentTimeMillis()}")
            }
        }
    }


    fun createCoroutines() {
        thread(start = true) {
            var c = 0
            for (i in 1..1_000_000)
                GlobalScope.launch {
                    c++
                }
            println("coroutines end")
        }
    }

    fun createThreads() {
        thread(start = true) {
            var c = 0
            for (i in 1..1_000_000)
                thread(start = true) {
                    c++
                }
            println("threads end")
        }
    }


    private fun main22() {
        //多个协程中suspend函数
        suspend fun getA() {
            delay(300)
            println("getA 开始执行，时间:  ${System.currentTimeMillis()}")
        }

        suspend fun getB() {
            delay(100)
            println("getB 开始执行，时间:  ${System.currentTimeMillis()}")
        }

        suspend fun getC() {
            delay(100)
            println("getC 开始执行，时间:  ${System.currentTimeMillis()}")
        }

        // 运行时
        GlobalScope.launch(Dispatchers.Main) {
            println("协程 开始执行，时间:  ${System.currentTimeMillis()}")
            GlobalScope.launch(Dispatchers.Main) {
                getA()
            }
            GlobalScope.launch(Dispatchers.Main) {
                getB()
            }
            GlobalScope.launch(Dispatchers.Main) {
                getC()
            }
        }
    }

    private fun main21() {
        GlobalScope.launch(Dispatchers.Unconfined) {
            for (i in 1..3) {
                println("协程任务一: 打印第$i 次，时间: ${System.currentTimeMillis()}")
            }
        }
        GlobalScope.launch(Dispatchers.Unconfined) {
            for (i in 1..3) {
                println("协程任务二: 打印第$i 次，时间: ${System.currentTimeMillis()}")
            }
        }
    }

    private fun main20() {
        System.out.println("main start")
        runBlocking {
            delay(1000L)
            System.out.println("delay......1000")
        }
        System.out.println("main end ")
    }


    private fun main19() {
        GlobalScope.launch {
            System.out.println("start")
            delay(1000)
            System.out.println("end")
        }
    }

    private fun main18() {
        //启动一个协程
        var job = GlobalScope.launch(Dispatchers.Unconfined, CoroutineStart.LAZY) {
            System.out.println("start")
            delay(1000)
            System.out.println("end")
        }
        job.start()
    }


    private fun main17() {


        System.out.println("Thread ${Thread.currentThread().name}")

        var result = GlobalScope.async {
            delay(1000)
            System.out.println("Thread ${Thread.currentThread().name}")
            return@async "result"
        }

//        System.out.println(result.await())

        System.out.println("main")
    }

    private fun main16() {
        var s = System.currentTimeMillis()
        thread {
            println("the first start Thread")
            println("Thread name start:${Thread.currentThread().name}")
            Thread.sleep(200)
            println("Thread end name :${Thread.currentThread().name}")
            println("the first end Thread")
        }.start()

        thread {
            println("the second start Thread")
            println("Thread start name :${Thread.currentThread().name}")
            Thread.sleep(100)
            println("Thread name start:${Thread.currentThread().name}")
            println("the second start Thread")
        }.start()
        Thread.sleep(500)

        println("Thread time ${System.currentTimeMillis() - s}")

    }

    private suspend fun calculate() {
        for (b in 0..1_000_000_000) {
            var a = Math.random() * 10
            println(a)
        }
    }

    private fun main15() {
        var s = System.currentTimeMillis()

        // 创建一个单线程的协程调度器，下面两个协程都运行在这同一线程上
        val coroutineDispatcher = newSingleThreadContext("ctx")
        // 启动协程 1
        GlobalScope.launch {
            println("the first start coroutine")
            println("Thread 1 name start:${Thread.currentThread().name}")
//            delay(200)
            calculate()
            println("Thread 1 end name :${Thread.currentThread().name}")
            println("the first end coroutine")
        }
        // 启动协程 2
        GlobalScope.launch {
            println("the second start coroutine")
            println("Thread 2 start name :${Thread.currentThread().name}")
            delay(100)
            println("Thread 2 name start:${Thread.currentThread().name}")
            println("the second start coroutine")
        }
        // 保证 main 线程存活，确保上面两个协程运行完成
        Thread.sleep(500)
        println("GlobalScope time ${System.currentTimeMillis() - s}")

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
            System.out.println("start")
            var result = GlobalScope.async {
                System.out.println("delay......1000")
                delay(1000)
                return@async "result"
            }
            System.out.println("result: ${result.await()}")
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
//        gitHubServiceApi.getUser("bennyhuo").enqueue(object : Callback<User> {
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                System.out.println("Thread " + Thread.currentThread().name)
//                Toast.makeText(this@MainActivity, "onFailure", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onResponse(call: Call<User>, response: Response<User>) {
//                System.out.println("Thread " + Thread.currentThread().name)
//                Toast.makeText(this@MainActivity, "onResponse", Toast.LENGTH_SHORT).show()
//            }
//        })


        GlobalScope.launch(Dispatchers.Unconfined) {
            try {
                System.out.println(Thread.currentThread().name)
                System.out.println(gitHubServiceApi.getUser2("bennyhuo").await())
                System.out.println(Thread.currentThread().name)

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
        suspend fun getToken(): String {
            delay(50)
            println("getToken 开始执行，时间:  ${System.currentTimeMillis()}")
            return "ask"
        }

        suspend fun getResponse(token: String): String {
            delay(100)
            println("getResponse 开始执行$token，时间:  ${System.currentTimeMillis()}")
            return "response"
        }

        fun setText(response: String) {
            println("setText 开始执行$response ，时间:  ${System.currentTimeMillis()}")
        }

        // 运行时
        GlobalScope.launch(Dispatchers.Main) {
            println("协程 开始执行，时间:  ${System.currentTimeMillis()}")
            val token = getToken()
            val response = getResponse(token)
            setText(response)
        }

        GlobalScope.launch(Dispatchers.Main) {
            //            delay(30)
            for (i in 1..1_000) {
                print("协程 $i")
            }
            println(" ")
            println("协程 2 开始执行，时间:  ${System.currentTimeMillis()}")
        }
        println("main ${System.currentTimeMillis()}")
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
