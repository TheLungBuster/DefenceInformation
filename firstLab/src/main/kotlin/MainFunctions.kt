import utils.Row
import utils.getLong
import utils.isPrime
import kotlin.math.sqrt
import kotlin.random.Random

class MainFunctions {

    fun doFastPow(number: Long, pow: Long, mod: Long): Long {
        val currentStep = number % mod
        return when {
            pow == 0L -> 1
            pow % 2 == 0L -> doFastPow((currentStep * currentStep) % mod, pow / 2, mod)
            else -> currentStep * doFastPow(currentStep, pow - 1, mod) % mod
        }
    }

    private var base: Long = 0
    private var degree: Long = 0
    private var modD: Long = 0

    fun doFastPowBiggerThanZero(number: Long, pow: Long, mod: Long): Long {
        base = number
        degree = pow
        modD = mod

        val n = Integer.toBinaryString(degree.toInt()).length
        var binaryNum = LongArray(n)
        binaryNum = intToBin(binaryNum)
        val values = LongArray(n)
        values[0] = base % modD
        for (k in 1 until n) {
            values[k] = values[k - 1] * values[k - 1] % modD
        }
        var j = 0
        while (j < n && binaryNum[j] == 0L) {
            j++
        }
        if (j >= values.size) {
            return 1 % modD
        }
        var result = values[j]
        j++
        while (j < n) {
            if (binaryNum[j] > 0) {
                result = result * values[j] % modD
            }
            j++
        }
        return result
    }

    private fun intToBin(bin: LongArray): LongArray {
        var i = 0
        while (degree >= 1) {
            bin[i] = degree % 2
            degree /= 2
            i++
        }
        return bin
    }

    fun funcNOD(a: Long, b: Long): Row {
        var A = a
        var B = b
        var u = Row(A, 1, 0)
        var v = Row(B, 0, 1)
        var q: Long
        val t = Row(0, 0, 0)
        do {
            q = A / B
            t.first = A % B
            t.second = u.second - q * v.second
            t.third = u.third - q * v.third
            A = B
            B = t.first
            u = v.copy()
            v = t.copy()
        } while (t.first != 0L)
        return u
    }

    fun generalKey(q: Long, p: Long, xA: Long, xB: Long): Long {
        if (!isPrime(q) && !isPrime(p) && (2 * q + 1) != p)
            throw Exception("q and p should be a prime number")
        var g = Random.getLong(p - 1)
        while (doFastPow(g, q, p) == 1L) {
            g = Random.getLong(p - 1)
        }
        val yA = doFastPow(g, xA, p)
        val yB = doFastPow(g, xB, p)
        val zAB = doFastPow(yB, xA, p)
        val zBA = doFastPow(yA, xB, p)
        if (zAB == zBA) return zAB
        else throw Exception("There is no general key")
    }

    fun babyGiantStep(a: Long, p: Long, y: Long): Long {
        if (y > p) throw Exception("'Y' should be less than 'P'")
        val m = sqrt(p.toDouble()).toLong() + 1
        var j = 0
        var i = 1
        var babyStep = mutableMapOf<Long, Int>()
        val giantStep = mutableMapOf<Long, Int>()
        while (j < m) {
            babyStep[doFastPow(a, j.toLong(), p) * y % p] = j.also { j++ }
        }
        var giantStepIteration: Long
        while (i <= m) {
            giantStepIteration = doFastPow(a, i * m, p)
            if (babyStep.containsKey(giantStepIteration)) {
                giantStep[giantStepIteration] = i
                break
            }
            i++
        }
        giantStep.map { it ->
            if (babyStep.containsKey(it.key)) {
                babyStep[it.key]?.let { value ->
                    j = value
                }
                giantStep[it.key]?.let { value ->
                    i = value
                }
                val x = i * m - j
                val answer = doFastPow(a, x, p)
                print(
                    "Answer for:\n" +
                            "A         := $a\n" +
                            "Y         := $y\n" +
                            "P         := $p\n" +
                            "Answer    := ${answer}\n"
                )
                return x
            }
        }
        print(
            "There is no answer for:\n" +
                    "A                     := $a\n" +
                    "Y                     := $y\n" +
                    "P                     := $p\n"
        )
        return -1
    }
}