import utils.getLong
import utils.isPrime
import kotlin.random.Random

class Player {

    private var P: Long = 0
    var C: Long = 0
    var D: Long = 0

    private var pair = MutableList<Long>(2) { 0 }
    fun getPair() = pair

    private val util = MainFunctions()
    private var rand = Random(0)
    fun setRandom(seed: Int) { rand = Random(seed) }


    var id: Long = 0

    @Throws(ArithmeticException::class)
    fun setPrime(generatedP: Long) {
        if (!isPrime(generatedP)) {
            throw ArithmeticException("Player $id says 'P is not prime, game cannot be started'")
        }
        id = rand.getLong()
        P = generatedP
        genSecretKeys()
    }

    private fun genSecretKeys() {
        var check: Long
        do {
            C = rand.getLong(P - 1)
            util.funcNOD(C, P - 1).also {
                check = it.first
                D = it.second
            }
            if (D < 0) D += P - 1
        } while (check != 1L)
    }

    fun shuffleDeck(deck: MutableList<Long>): MutableList<Long> {
        val shuffledDeck = deck.map {
            util.doFastPow(it, C, P)
        }
        return shuffledDeck.shuffled().toMutableList()
    }

    fun setPair(first: Long, second: Long) {
        pair[0] = first
        pair[1] = second
        println(
            """
               Player($id) get his crypted pair: $pair
               
            """.trimIndent()
        )
    }

    fun setDecryptedPair(decryptedPair: MutableList<Long>) {
        println(
            """
               Finally player($id) get his decrypted pair: $pair
               
            """.trimIndent()
        )
        decryptPair(decryptedPair)
    }

    private fun decryptPair(decryptedPair: MutableList<Long>) {
        pair = decryptedPair.map {
            util.doFastPowBiggerThanZero(it, D, P)
        }.toMutableList()
    }

    fun decryptAnotherPlayerCards(pair: MutableList<Long>): MutableList<Long> {
        println(
            """
               Player($id) get cards: $pair
            """.trimIndent()
        )
        return pair.map {
            util.doFastPowBiggerThanZero(it, D, P)
        }.toMutableList()
    }
}