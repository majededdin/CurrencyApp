package com.majed.currencyapp.fibonacci

object FibonacciIterativeNumber {
    fun iterative(n: Long): Long {
        if (n < 2) return n
        var minusOne: Long = 1
        var minusTwo: Long = 0
        var result = minusOne
        for (i in 2..n) {
            result = minusOne + minusTwo
            minusTwo = minusOne
            minusOne = result
        }
        return result
    }
}