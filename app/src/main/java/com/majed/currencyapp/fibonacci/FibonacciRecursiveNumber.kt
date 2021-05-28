package com.majed.currencyapp.fibonacci

object FibonacciRecursiveNumber {
    fun recursive(n: Long): Long = if (n < 2) n else recursive(n - 1) + recursive(n - 2)
}