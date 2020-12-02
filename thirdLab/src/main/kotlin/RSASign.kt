import utils.getLong
import utils.hash.hashDocWithMD5
import utils.isPrime
import java.io.File
import java.security.MessageDigest
import kotlin.experimental.and
import kotlin.random.Random
import kotlin.system.exitProcess

class RSASign {

    companion object {
        const val path = "app/src/main/resources/sign"
        const val original = "destroy.jpg"
        const val signed = "destroySignedWithRSA.txt"
    }

    private var N: Long = 0
    private var fi: Long = 0
    private var c: Long = 0
    private var d: Long = 0

    private lateinit var md5Hash: ByteArray
    private lateinit var y: LongArray

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

    fun sign() {
        val inputFile = File("$path/$original")
        val signedFile = File("$path/$signed")

        md5Hash = hashDocWithMD5(inputFile.readBytes())

        y = LongArray(md5Hash.size)
        val s = LongArray(md5Hash.size)

        var i = 0

        while (i < md5Hash.size) {
            if (md5Hash[i] < 0) {
                y[i] = (md5Hash[i].toInt() and 0xff).toLong()
            } else {
                y[i] = md5Hash[i].toLong()
            }
            i++
        }
        i = 0

        signedFile.printWriter().use { file ->
            while (i < md5Hash.size) {
                s[i] = util.doFastPow(y[i], c, N)
                file.println(s[i])
                i++
            }
        }

        println("Document signed")
    }

    fun checkSign() {
        val signedFile = File("$path/$signed")

        val sFromSigned = LongArray(md5Hash.size)
        val w = LongArray(md5Hash.size)
        var i = 0

        val longList = signedFile.readLines()
        while (i < md5Hash.size) {
            sFromSigned[i] = longList[i].toLong()
            w[i] = util.doFastPow(sFromSigned[i], d, N)
            if (y[i] != w[i]) {
                println("RSA Signature is not valid")
                exitProcess(-1)
            }
            i++
        }
        println("RSA Signature is valid")
    }
}