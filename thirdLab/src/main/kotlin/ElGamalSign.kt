import utils.getLong
import utils.hash.hashDocWithMD5
import utils.isPrime
import java.io.File
import kotlin.random.Random
import kotlin.system.exitProcess

class ElGamalSign {

    companion object {
        const val path = "app/src/main/resources/sign"
        const val original = "destroy.jpg"
        const val signed = "destroySignedWithElgamal.txt"
    }

    private lateinit var md5Hash: ByteArray
    private lateinit var h: LongArray

    private var p: Long = 0
    private var g: Long = 0
    private var x: Long = 0
    private var y: Long = 0
    private var r: Long = 0
    private var k: Long = 0
    private var kInv: Long = 0
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
        x = rand.nextLong(1, p - 1)

        //Открытый ключ d
        y = util.doFastPow(g, x, p)

        k = rand.getLong(p - 1)
        while (util.funcNOD(k, p - 1).first != 1L) {
            k -= 1
        }

        r = util.doFastPow(g, k, p)

        kInv = util.funcNOD(p - 1, k).third
        if (kInv < 0) kInv += p - 1
    }

    fun sign() {
        val inputFile = File("$path/$original")
        val signedFile = File("$path/$signed")
        md5Hash = hashDocWithMD5(inputFile.readBytes())

        val u = LongArray(md5Hash.size)
        val s = LongArray(md5Hash.size)
        h = LongArray(md5Hash.size)

        var i = 0

        while (i < md5Hash.size) {
            h[i] = if (md5Hash[i] < 0) {
                (md5Hash[i].toInt() and 0xff).toLong()
            } else {
                md5Hash[i].toLong()
            }

            u[i] = util.doFastPow(h[i] - x * r, 1, p - 1)
            if (u[i] < 0) {
                u[i] += p - 1
            }
            s[i] = util.doFastPow(u[i] * kInv, 1, p - 1)
            i++
        }

        signedFile.printWriter().use { file ->
            s.map {
                file.println(it)
            }
        }
        println("Document signed")
    }

    fun checkSign() {
        val signedFile = File("$path/$signed")

        val sFromSigned = LongArray(md5Hash.size)
        val result = LongArray(md5Hash.size)
        var i = 0

        val longList = signedFile.readLines()
        while (i < md5Hash.size) {
            sFromSigned[i] = longList[i].toLong()
            result[i] = util.doFastPow(y, r, p) * util.doFastPow(r, sFromSigned[i], p) % p
            if (result[i].also { print("$it == ") } != util.doFastPow(g, h[i].also { print("(h = $it) ") }, p).also { println(
                    it
                ) }) {
                println("Error. Signature is not valid")
                exitProcess(-1)
            }
            i++
        }
        println("ElGamal Signature is valid")
    }
}