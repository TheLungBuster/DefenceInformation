import utils.hash.hashDocWithMD5toString
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.security.SignatureException
import kotlin.random.Random
import kotlin.system.exitProcess

class GOSTSign {
    private var P: BigInteger
    private var Q: BigInteger
    private var A: BigInteger
    private var G: BigInteger

    private var X: BigInteger
    private var Y: BigInteger

    private val util = MainFunctions()
    private val rand = Random(1)

    private lateinit var md5Hash: String
    private lateinit var h: BigInteger
    private lateinit var r: BigInteger
    private lateinit var s: BigInteger

    companion object {
        const val path = "app/src/main/resources/sign"
        const val original = "destroy.jpg"
        const val signed = "destroySignedWithGOST.txt"
    }

    init {
        do {
            do {
                Q = getRandomBigInteger(16)
                P = getRandomBigInteger(31)
            } while (!P.isProbablePrime(1) && !Q.isProbablePrime(1))
        } while (P.minus(BigInteger.ONE).mod(Q) != BigInteger.ZERO)

        val B = P.minus(BigInteger.ONE).divide(Q)

        do {
            G = getRandomBigInteger(16)
            A = G.modPow(P.subtract(BigInteger.ONE).divide(Q), P)
        } while (A < BigInteger.ONE)

        X = BigInteger.valueOf(rand.nextLong(1, Q.longValueExact() + 1))// Secret key
        Y = A.pow(X.intValueExact()).mod(P)// Open key
    }

    fun sign() {
        val inputFile = File("$path/$original")
        val signedFile = File("$path/$signed")
        md5Hash = hashDocWithMD5toString(inputFile.readBytes())

        h = BigInteger(md5Hash, 16)
        h = h.mod(BigInteger(Q.toString()))
        var k: Long

        do {
            k = rand.nextLong(0L, Q.longValueExact() + 1)
            r = A.modPow(BigInteger.valueOf(k), P).mod(Q)//util.doFastPow(A, k[i], P) % Q)
            s = X.multiply(r).add(h.multiply(BigInteger.valueOf(k)))
                .mod(Q)//k[i] * h[i] + X * r[i]) % Q).also { println("s = $it") }
        } while (r == BigInteger.ZERO || s == BigInteger.ZERO)

        signedFile.printWriter().use { file ->
            file.println(s)
        }
        println("Document signed")
    }

    fun checkSign() {
        val uFirst: BigInteger
        val uSecond: BigInteger
        val v: BigInteger
        if (r < BigInteger.ZERO || s > Q) {
            throw SignatureException("digital signature gost is invalid")
        }
        val hInverse = h.modInverse(Q)

        uFirst = s.multiply(hInverse).mod(Q)
        uSecond = r.negate().multiply(hInverse).mod(Q)
        v = A.pow(uFirst.intValueExact()).multiply(Y.pow(uSecond.intValueExact())).mod(P).mod(Q)

        if (v.compareTo(r) != 0) {
            println("GOST sign is not valid")
            exitProcess(-1)
        }
        println("GOST sign is valid")
    }

    private fun getRandomBigInteger(numBits: Int): BigInteger {
        val number = BigInteger(numBits, java.util.Random()) //Give you a number between 0 and 2^numBits - 1
        return number.setBit(0) //Set the first bit so number is between 2^511 and 2^512 - 1
    }
}