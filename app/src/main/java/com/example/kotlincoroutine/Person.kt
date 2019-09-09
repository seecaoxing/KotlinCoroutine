package com.example.kotlincoroutine

import android.R.attr.name
import android.R.attr.name


class Person(private var name: String, private var age: Int) {

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getAge(): Int {
        return age
    }

    fun setAge(age: Int) {
        this.age = age
    }

}