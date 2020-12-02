import utils.getLong
import utils.isPrime
import kotlin.random.Random
import kotlin.system.exitProcess

class TexasHoldemRoom(var players: Int) {

    var playersList: MutableList<Player>
    var board = MutableList<Long>(5) { 0 }
    private var deck = mutableListOf<Long>(
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
        28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52
    )
    private var P: Long = 0
    private val rand = Random(10)

    init {
        if (players < 2) {
            println("Not enough players, game won't start until more players will come")
            exitProcess(-1)
        } else if (players > 23) {
            println("We already got 23 players, you should find another spot, pall")
            players = 23
        }
        playersList = MutableList(players) { Player() }
        var Q: Long
        do {
            Q = rand.getLong()
            P = 2 * Q + 1
        } while (!isPrime(P) && !isPrime(Q))
        println("Players decided that Prime will be $P\n")
        playersList.forEach { player ->
            player.setRandom(rand.nextInt())
            player.setPrime(P)
        }

        for (player in playersList) {
            deck = player.shuffleDeck(deck)
        }
    }

    @ExperimentalStdlibApi
    fun distributionOfCards() {
        playersList.map { player ->
            val first = deck.first().also { deck.removeFirst() }
            val second = deck.first().also { deck.removeFirst() }
            player.setPair(first, second)
        }
        board = board.map {
            deck.last().also { deck.removeLast() }
        }.toMutableList()
    }

    @ExperimentalStdlibApi
    fun decrypt() {
        var i = 0
        var player: Player?
        var pair: MutableList<Long>
        while (i < playersList.size) {
            player = playersList.first().also { playersList.removeFirst() }
            pair = player.getPair()
            for (playa in playersList) {
                pair = playa.decryptAnotherPlayerCards(pair)
            }
            player.setDecryptedPair(pair).also {
                player?.let {
                    playersList.add(it)
                } ?: throw NullPointerException("Player was 'null'")
                player = null
            }
            i++
        }

        for (playa in playersList) {
            board = playa.decryptAnotherPlayerCards(board)
        }
    }

    fun report() {
        val log =
            """
                Board: ${board.map { deckMap[it] }}
                Players cards:
                ${playersList.map { player -> "id = ${player.id} , Pair = ${player.getPair().map { card -> deckMap[card] }}"}}
                Players keys: 
                ${playersList.map { "id = ${it.id}: [C::${it.C}, D::${it.D}]" }}
            """.trimIndent()
        println(log)
    }
}