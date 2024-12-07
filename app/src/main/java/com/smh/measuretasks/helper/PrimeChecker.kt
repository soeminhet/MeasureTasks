package com.smh.measuretasks.helper

import kotlin.math.sqrt

fun isPrime(number: Int): Boolean {
    if(number < 2) return false
    for (i in 2..sqrt(number.toDouble()).toInt()) {
        if (number % i == 0) return false
    }
    return true
}