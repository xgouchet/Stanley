package fr.xgouchet.packageexplorer.parser

/**
 * @return value of a Little Endian 32 bit word from the byte array at the given index
 */
fun ByteArray.getLEWord(index : Int) : Int {
    return (get(index + 3).toInt() shl 24 and -0x1000000
        or (get(index + 2).toInt() shl 16 and 0x00ff0000)
        or (get(index + 1).toInt() shl 8 and 0x0000ff00)
        or (get(index + 0).toInt() shl 0 and 0x000000ff))
}

/**
 * @return value of a Little Endian 16 bit word from the byte array at the given index
 */
fun ByteArray.getLEShort(off: Int): Int {
    return (get(off + 1).toInt() shl 8 and 0xff00
        or (get(off + 0).toInt() shl 0 and 0x00ff))
}