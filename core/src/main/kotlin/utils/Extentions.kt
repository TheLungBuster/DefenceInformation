package utils

import kotlin.random.Random


fun Random.getLong() : Long =
    nextLong(0, 1000000000)

fun Random.getLong(upperBound: Long) : Long =
    nextLong(0, upperBound)

fun isPrime(number: Long): Boolean {
    if (number % 2 == 0L) return false
    var i: Long = 3
    while (i * i <= number) {
        //checking ods
        if (number % i == 0L) {
            return false
        }
        i += 2
    }
    return true
}