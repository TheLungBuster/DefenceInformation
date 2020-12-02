import sun.security.krb5.internal.crypto.RsaMd5CksumType
import utils.Row
import utils.getLong
import utils.isPrime
import kotlin.random.Random

@ExperimentalStdlibApi
fun main() {

    val rand = Random(2)

    //val base = rand.getLong()
    //val pow = rand.getLong()
    //val mod = rand.getLong()
    //val output = MainFunctions.doFastPow(base, pow, mod)
    //var outputString = """
    //    number      = $base
    //    pow         = $pow
    //    mod         = $mod
    //    output      = $output
    //
    //""".trimIndent()
//
    //println("doFastPow")
    //println(outputString)
//
    //val a = rand.getLong()
    //val b = rand.getLong()
    //val row = MainFunctions.funcNOD(a, b)
    //outputString = """
    //    A           = $a
    //    B           = $b
    //    NOD         = ${row.first}
    //    X           = ${row.second}
    //    Y           = ${row.third}
    //
    //""".trimIndent()
//
    //println("funcNOD")
    //println(outputString)
//
    //var q = rand.getLong()
    //var p = 2 * q + 1
    //while (!isPrime(q) && !isPrime(p)) {
    //    q = rand.getLong()
    //    p = 2 * q + 1
    //}
    //val xA = rand.getLong()
    //val xB = rand.getLong()
    //val generalKey = MainFunctions.generalKey(q, p, xA, xB)
    //outputString = """
    //    q           = $q
    //    xA          = $xA
    //    xB          = $xB
    //    generalKey  = $generalKey
    //
    //""".trimIndent()
//
    //println("generalKey")
    //println(outputString)
//
    //val aBG = rand.getLong()
    //val pBG = rand.getLong()
    //val yBG = rand.getLong(pBG)
    //println("babyGiantStep")
    //MainFunctions.babyGiantStep(aBG, pBG, yBG)
    //var prime = rand.getLong()
    //while (!isPrime(prime)) {
    //    prime = rand.getLong()
    //}

    //val shamir = ShamirsProtocol(prime)
    //shamir.xOne()
    //shamir.xTwo()
    //shamir.xThree()
    //shamir.xFour()

    //val elGamal = ElGamalsProtocol()
    //elGamal.crypt()
    //elGamal.decrypt()

    //val rsa = RSAProtocol()
    //rsa.crypt()
    //rsa.decrypt()

    //val vernam = VernamProtocol()
    //vernam.generateKey()
    //vernam.crypt()
    //vernam.decrypt()
    //vernam.crypt()

    //val signRSA = RSASign()
    //signRSA.sign()
    //signRSA.checkSign()

    //val signElGamal = ElGamalSign()
    //signElGamal.sign()
    //signElGamal.checkSign()

    //val signGOST = GOSTSign()
    //signGOST.sign()
    //signGOST.checkSign()

    val holdemRoom = TexasHoldemRoom(4)
    holdemRoom.distributionOfCards()
    holdemRoom.decrypt()
    holdemRoom.report()
}