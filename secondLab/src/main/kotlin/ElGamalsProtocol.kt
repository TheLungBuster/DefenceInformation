import utils.getLong
import utils.isPrime
import java.io.File
import kotlin.random.Random

class ElGamalsProtocol {

    companion object {
        const val path = "app/src/main/resources/elgamal"
        const val original = "boom.jpg"
        const val crypt = "boomCrypt.txt"
        const val decrypt = "boomDecrypt.jpg"
    }

    private var p: Long = 0
    private var g: Long = 0
    private var xB: Long = 0
    private var yB: Long = 0
    private var r: Long = 0
    private val util = MainFunctions()
    private val rand = Random(1)

    init {
        var q: Long
        do {
            q = rand.getLong()
            p = 2 * q + 1
        } while (!isPrime(q) && !isPrime(p) || q > 1000000000)

        do {
            g = rand.getLong(p - 1)
        } while (util.doFastPow(g, q, p) == 1L)

        //Секретный ключ с
        xB = rand.nextLong(1, p - 1)
       
        //Открытый ключ d
        yB = util.doFastPow(g, xB, p)
    }

    fun crypt() {
        val inputFile = File("$path/$original")
        val outputFile = File("$path/$crypt")
        val byteList = inputFile.readBytes()

        val k = rand.nextLong(1, p - 2)
        r = util.doFastPow(g, k, p)

        outputFile.printWriter().use { file ->
            byteList.forEach { byte ->
                file.println(
                    ((byte.toLong() % p) * util.doFastPow(yB, k, p) % p)
                )
            }
        }
    }

    fun decrypt() {
        val inputFile = File("$path/$crypt")
        val outputFile = File("$path/$decrypt")
        val longList = inputFile.readLines().toList().map { it.toLong() }

        val byteList: ByteArray = longList.map { long ->
            ((long % p) * util.doFastPow(r, p - 1 - xB, p) % p).toByte()
        }.toByteArray()

        outputFile.writeBytes(byteList)
    }
}