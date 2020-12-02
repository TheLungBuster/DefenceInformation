import utils.getLong
import utils.isPrime
import java.io.File
import kotlin.random.Random

class RSAProtocol {

    companion object {
        const val path = "app/src/main/resources/rsa"
        const val original = "boom.jpg"
        const val crypt = "boomCrypt.txt"
        const val decrypt = "boomDecrypt.jpg"
    }

    private var N: Long = 0
    private var fi: Long = 0
    private var c: Long = 0
    private var d: Long = 0

    private val util = MainFunctions()
    private val rand = Random(1)

    init {
        var P: Long
        var Q: Long

        do {
            P = rand.getLong()
            while (!isPrime(P)) {
                P = rand.getLong(1000000)
            }

            Q = rand.getLong()
            while (!isPrime(Q)) {
                Q = rand.getLong(1000000)
            }

            N = Q * P
        } while (N > 1000000000)
        fi = (Q - 1) * (P - 1)

        d = rand.getLong()

        while (util.funcNOD(fi, d).first != 1L) {
            d -= 1
        }

        val evclid = util.funcNOD(fi, d)
        c = evclid.third
        if (c < 0) c += fi
    }

    fun crypt() {
        val inputFile = File("$path/$original")
        val outputFile = File("$path/$crypt")
        val byteList = inputFile.readBytes()

        outputFile.printWriter().use { file ->
            byteList.forEach { byte ->
                file.println(
                    util.doFastPow(byte.toLong(), d, N)
                )
            }
        }
    }

    fun decrypt() {
        val inputFile = File("$path/$crypt")
        val outputFile = File("$path/$decrypt")
        val longList = inputFile.readLines().toList().map { it.toLong() }

        val byteList: ByteArray = longList.map { long ->
            util.doFastPow(long, c, N).toByte()
        }.toByteArray()

        outputFile.writeBytes(byteList.also { print(it[0]) })
    }
}