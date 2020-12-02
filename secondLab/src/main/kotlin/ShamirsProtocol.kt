import utils.Row
import utils.getLong
import utils.isPrime
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.random.Random

class ShamirsProtocol(private val p: Long) {

    companion object {
        const val path = "app/src/main/resources/shamir"
        const val original = "destory.jpg"
        const val cryptX1 = "yesSP1.txt"
        const val cryptX2 = "yesSP2.txt"
        const val cryptX3 = "yesSP3.txt"
        const val decryptX4 = "destoryDecrypt.jpg"
    }

    private var cA: Long = 0
    private var dA: Long = 0
    private var cB: Long = 0
    private var dB: Long = 0
    private val util = MainFunctions()
    private val rand = Random(3)

    init {
        do {
            findCAPrime()
        } while (util.doFastPow((dA * cA), 1, (p - 1)) != 1L)

        do {
            findCBPrime()
        } while (util.doFastPow((dB * cB), 1, (p - 1)) != 1L)
    }

    fun xOne() {
        val inputFile = File("$path/$original")
        val outputFile = File("$path/$cryptX1")
        val byteList = inputFile.readBytes()
        outputFile.printWriter().use { file ->
            byteList.forEach { byte ->
                file.println(util.doFastPow(byte.toLong(), cA, p))
            }
        }
    }

    fun xTwo() {
        val inputFile = File("$path/$cryptX1")
        val outputFile = File("$path/$cryptX2")
        val longList = inputFile.readLines().toList().map { it.toLong() }
        outputFile.printWriter().use { file ->
            longList.forEach { long ->
                file.println(util.doFastPow(long, cB, p))
            }
        }
    }

    fun xThree() {
        val inputFile = File("$path/$cryptX2")
        val outputFile = File("$path/$cryptX3")
        val longList = inputFile.readLines().toList().map { it.toLong() }
        outputFile.printWriter().use { file ->
            longList.forEach { long ->
                file.println(util.doFastPow(long, dA, p))
            }
        }
    }

    fun xFour() {
        val inputFile = File("$path/$cryptX3")
        val outputFile = File("$path/$decryptX4")
        val longList = inputFile.readLines().toList().map { it.toLong() }
        val byteList: ByteArray = longList.map { long ->
            util.doFastPow(long, dB, p).toByte()
        }.toByteArray().also {
            println(it[1])
        }
        outputFile.writeBytes(byteList)
    }

    private fun findCAPrime() {
        var evclid: Row
        do {
            cA = 4
            while (!isPrime(cA) && cA % 2L != 1L) cA = rand.getLong()
            evclid = util.funcNOD(p - 1, cA)
            dA = evclid.third
        } while (evclid.first != 1L && dA < p)
        if (dA < 0) dA = dA + p - 1
    }

    private fun findCBPrime() {
        var evclid: Row
        do {
            cB = 4
            while (!isPrime(cB) && cB % 2L != 1L) cB = rand.getLong()
            evclid = util.funcNOD(p - 1, cB)
            dB = evclid.third
        } while (evclid.first != 1L && dB < p)
        if (dB < 0) dB = dB + p - 1
    }
}