import utils.getLong
import java.io.File
import kotlin.random.Random

class VernamProtocol {

    companion object {
        const val path = "app/src/main/resources/vernam"
        const val original = "boom.jpg"
        const val crypt = "boomCrypt.txt"
        const val decrypt = "boomDecrypt.jpg"
    }

    private var shifr = mutableListOf<Long>()
    private val inputFile = File("$path/$original")

    private val rand = Random(1)

    fun generateKey() {
        inputFile.readBytes().forEach {
            shifr.add(rand.getLong())
        }
    }

    fun crypt() {
        if (shifr.isNullOrEmpty()) {
            println("Отстуствует ключ, для генерации используйте функцию generateKey()")
            return
        }
        val byteList = inputFile.readBytes()
        val outputFile = File("$path/$crypt")

        outputFile.printWriter().use { file ->
            var i = 0
            while (byteList.size > i) {
                file.println(shifr[i] xor byteList[i].toLong())
                i++
            }
        }
    }

    fun decrypt() {
        if (shifr.isNullOrEmpty()) {
            println("Отстуствует ключ, для генерации используйте функцию generateKey()")
            return
        }

        val inputFile = File("$path/$crypt")
        val outputFile = File("$path/$decrypt")
        val longList = inputFile.readLines().toList().map { it.toLong() }.toMutableList()
        val byteList: ByteArray

        var i = 0
        while (longList.size > i) {
            longList[i] = longList[i] xor shifr[i]
            i++
        }

        byteList = longList.map {
            it.toByte()
        }.toByteArray()

        shifr = mutableListOf()
        outputFile.writeBytes(byteList)
    }
}