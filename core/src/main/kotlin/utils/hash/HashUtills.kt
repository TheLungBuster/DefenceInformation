package utils.hash

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

fun hashDocWithMD5(input: ByteArray): ByteArray {
    return MessageDigest
        .getInstance("MD5")
        .digest(input)
}

fun hashDocWithMD5toString(input: ByteArray): String {
    val hash = MessageDigest
        .getInstance("MD5")
        .digest(input)
    return DatatypeConverter.printHexBinary(hash).also { println(it) }
}