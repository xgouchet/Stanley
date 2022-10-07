package fr.xgouchet.packageexplorer.parser

import java.io.IOException
import java.io.InputStream
import java.lang.Boolean.toString
import java.text.DecimalFormat
import timber.log.Timber

class CompressedXmlParser {

    private lateinit var listener: CompressedXmlParserListener
    private val namespaces = mutableMapOf<String, String>()
    private lateinit var data: ByteArray

    private lateinit var stringsTable: Array<String?>
    private lateinit var resourcesIds: IntArray

    private var stringsCount = 0
    private var resCount: Int = 0

    /**
     * Parses the input stream as Compressed XML,
     *
     * @param input    the source input to parse
     * @param listener the listener for XML events (must not be null)
     * @throws IOException if the input can't be read
     */
    @Throws(IOException::class)
    fun parse(
        input: InputStream,
        listener: CompressedXmlParserListener
    ) {
        this.listener = listener
        input.use {
            data = input.readBytes()
        }
        input.close()

        parseCompressedXml()
    }

    // region Parsing

    private fun parseCompressedXml() {
        var idx = 0

        while (idx < data.size) {
            val chunkWord = data.getLEWord(idx)

            val chunkSize: Int = when (chunkWord) {
                WORD_START_DOCUMENT -> parseStartDocument()
                WORD_STRING_TABLE -> parseStringTable(idx)
                WORD_RES_TABLE -> parseResourceTable(idx)
                WORD_START_NS -> parseNamespace(idx, true)
                WORD_END_NS -> parseNamespace(idx, false)
                WORD_START_TAG -> parseStartTag(idx)
                WORD_END_TAG -> parseEndTag(idx)
                WORD_TEXT -> parseText(idx)
                WORD_EOS -> {
                    listener.endDocument()
                    WORD_SIZE
                }
                else -> {
                    Timber.w("Unknown chunk word $chunkWord")
                    WORD_SIZE
                }
            }
            idx += chunkSize
        }

        listener.endDocument()
    }

    /**
     * A doc starts with the following 4bytes words :
     *
     *  * 0th word : 0x00080003
     *  * 1st word : chunk size
     * @return the size of this token, in bytes
     */
    private fun parseStartDocument(): Int {
        listener.startDocument()
        return 2 * WORD_SIZE
    }

    /**
     * the string table starts with the following 4bytes words :
     *
     *  * 0th word : 0x1c0001
     *  * 1st word : chunk size
     *  * 2nd word : number of string in the string table
     *  * 3rd word : number of styles in the string table
     *  * 4th word : flags - sorted/utf8 flag (0)
     *  * 5th word : Offset to String data
     *  * 6th word : Offset to style data
     * @return the size of this token, in bytes
     */
    private fun parseStringTable(chunkStartIndex: Int): Int {
        val chunkSize = data.getLEWord(wordIndex(chunkStartIndex, 1))
        stringsCount = data.getLEWord(wordIndex(chunkStartIndex, 2))
        stringsTable = arrayOfNulls(stringsCount)

        val stringDataOffset = data.getLEWord(wordIndex(chunkStartIndex, 5))
        val strDataStart: Int = (chunkStartIndex + stringDataOffset)

        var strAddress: Int
        for (i in 0 until stringsCount) {
            val strOffset = data.getLEWord(wordIndex(chunkStartIndex, i + 7))
            strAddress = (strDataStart + strOffset)
            stringsTable[i] = getStringFromStringTable(strAddress)
        }
        return chunkSize
    }

    /**
     * the resource ids table starts with the following 4bytes words :
     *
     *  * 0th word : 0x00080180
     *  * 1st word : chunk size
     * @return the size of this token, in bytes
     */
    private fun parseResourceTable(chunkStartIndex: Int): Int {
        val chunkSize = data.getLEWord(wordIndex(chunkStartIndex, 1))
        resCount = (chunkSize / WORD_SIZE) - 2
        resourcesIds = IntArray(resCount)

        for (i in 0 until resCount) {
            resourcesIds[i] = data.getLEWord(wordIndex(chunkStartIndex, i + 2))
        }
        return chunkSize
    }

    /**
     * A namespace tag contains the following 4bytes words :
     *
     *  * 0th word : 0x00100100 = Start NS / 0x00100101 = end NS
     *  * 1st word : chunk size
     *  * 2nd word : line this tag appeared
     *  * 3rd word : optional xml comment for element (usually 0xFFFFFF)
     *  * 4th word : index of namespace prefix in StringIndexTable
     *  * 5th word : index of namespace uri in StringIndexTable
     * @return the size of this token, in bytes
     */
    private fun parseNamespace(chunkStartIndex: Int, isStartTag: Boolean): Int {
        val chunkSize = data.getLEWord(wordIndex(chunkStartIndex, 1))
        val prefixIdx = data.getLEWord(wordIndex(chunkStartIndex, 4))
        val uriIdx = data.getLEWord(wordIndex(chunkStartIndex, 5))

        val prefix = lookupString(prefixIdx)
        val uri = lookupString(uriIdx)

        if (uri.isNullOrBlank()) {
            Timber.w("Parsing invalid namespace with uri:$uri")
        } else {
            if (isStartTag) {
                listener.startPrefixMapping(prefix, uri)
                namespaces[uri] = prefix.orEmpty()
            } else {
                listener.endPrefixMapping(prefix, uri)
                namespaces.remove(uri)
            }
        }
        return chunkSize
    }

    /**
     * A start tag will start with the following 4bytes words :
     *
     *  * 0th word : 0x00100102 = Start_Tag
     *  * 1st word : chunk size
     *  * 2nd word : line this tag appeared in the original file
     *  * 3rd word : optional xml comment for element (usually 0xFFFFFF)
     *  * 4th word : index of namespace uri in StringIndexTable, or 0xFFFFFFFF for default NS
     *  * 5th word : index of element name in StringIndexTable
     *  * 6th word : size of attribute structures to follow
     *  * 7th word : number of attributes following the start tag
     *  * 8th word : index of id attribute (0 if none)
     * @return the size of this token, in bytes
     */
    private fun parseStartTag(chunkStartIndex: Int) : Int {
        val chunkSize = data.getLEWord(wordIndex(chunkStartIndex, 1))
        val uriIdx = data.getLEWord(wordIndex(chunkStartIndex, 4))
        val nameIdx = data.getLEWord(wordIndex(chunkStartIndex, 5))
        // val attrChunkSize = data.getLEWord(wordIndex(chunkStartIndex, 6))
        val attrCount = data.getLEWord(wordIndex(chunkStartIndex, 7))
        // val idAttrIndex = data.getLEWord(wordIndex(chunkStartIndex, 8))

        val elementTag = identifier(uriIdx, nameIdx)

        // offset to start of attributes
        val attrs = mutableListOf<Attribute>()
        for (a in 0 until attrCount) {
            val attrChunkStartIndex = wordIndex(chunkStartIndex, 9 + (a * 5))
            attrs.add(parseAttribute(attrChunkStartIndex))
        }
        listener.startElement(elementTag, attrs)
        return chunkSize
    }

    /**
     * An attribute will have the following 4bytes words :
     *
     *  * 0th word : index of namespace uri in StringIndexTable,
     *      or 0xFFFFFFFF for default NS
     *  * 1st word : index of attribute name in StringIndexTable
     *  * 2nd word : index of attribute value in StringIndexTable,
     *      or 0xFFFFFFFF if value is a typed value
     *  * 3rd word : value type
     *  * 4th word : resource id value
     */
    private fun parseAttribute(chunkStartIndex: Int): Attribute {
        val uriIdx = data.getLEWord(wordIndex(chunkStartIndex, 0))
        val nameIdx = data.getLEWord(wordIndex(chunkStartIndex, 1))
        val strValueIdx = data.getLEWord(wordIndex(chunkStartIndex, 2))
        val valueType = data.getLEWord(wordIndex(chunkStartIndex, 3))
        val valueData = data.getLEWord(wordIndex(chunkStartIndex, 4))

        val identifier = identifier(uriIdx, nameIdx)

        val value = if (strValueIdx == -0x1) {
            getAttributeValue(valueType, valueData)
        } else {
            lookupString(strValueIdx)
        }
        return Attribute(identifier, value)
    }

    /**
     * A text will start with the following 4bytes word :
     *
     *  * 0th word : 0x00100104 = Text
     *  * 1st word : chunk size
     *  * 2nd word : line this element appeared in the original document
     *  * 3rd word : optional xml comment for element (usually 0xFFFFFF)
     *  * 4rd word : string index in string table
     *  * 5rd word : ??? (always 8)
     *  * 6rd word : ??? (always 0)
     * @return the size of this token, in bytes
     */
    private fun parseText(chunkStartIndex: Int): Int {
        val chunkSize = data.getLEWord(wordIndex(chunkStartIndex, 1))
        val strIndex = data.getLEWord(wordIndex(chunkStartIndex, 4))
        val data = lookupString(strIndex).orEmpty()
        listener.text(data)
        return chunkSize
    }

    /**
     * EndTag contains the following 4bytes words :
     *
     *  * 0th word : 0x00100103 = End_Tag
     *  * 1st word : chunk size
     *  * 2nd word : line this tag appeared in the original file
     *  * 3rd word : optional xml comment for element (usually 0xFFFFFF)
     *  * 4th word : index of namespace name in StringIndexTable, or 0xFFFFFFFF
     * for default NS
     *  * 5th word : index of element name in StringIndexTable
     * @return the size of this token, in bytes
     */
    private fun parseEndTag(chunkStartIndex: Int): Int {
        val chunkSize = data.getLEWord(wordIndex(chunkStartIndex, 1))
        val uriIdx = data.getLEWord(wordIndex(chunkStartIndex, 4))
        val nameIdx = data.getLEWord(wordIndex(chunkStartIndex, 5))
        val elementTag = identifier(uriIdx, nameIdx)
        listener.endElement(elementTag)
        return chunkSize
    }

    // endregion

    // region utils

    /**
     * @param offset offset of the beginning of the string inside the StringTable
     * (and not the whole data array)
     * @return the String
     */
    private fun getStringFromStringTable(offset: Int): String {
        val strLength: Int
        val chars: ByteArray

        val sizeB0 = data[offset].toInt()
        val sizeB1 = data[offset + 1].toInt()
        if (sizeB1 == sizeB0) {
            strLength = sizeB0
            chars = ByteArray(strLength)
            for (i in 0 until strLength) {
                chars[i] = data.get(offset + 2 + i)
            }
        } else {
            strLength = (sizeB1 shl 8 and 0xFF00 or (sizeB0 and 0xFF))
            chars = ByteArray(strLength)
            for (i in 0 until strLength) {
                chars[i] = data[offset + 2 + i * 2]
            }
        }
        return String(chars)
    }

    private fun wordIndex(chunkStartIndex: Int, wordPosition: Int): Int {
        return chunkStartIndex + (wordPosition * WORD_SIZE)
    }

    /**
     * @param index the index of the string in the StringIndexTable
     * @return the string
     */
    private fun lookupString(index: Int): String? {
        return if (index in 0 until stringsCount) {
            stringsTable[index]
        } else {
            null
        }
    }

    /**
     * @param nameIdx the index of the name in the StringIndexTable
     * @param uriIdx the index of the namespace URI in the StringIndexTable
     */
    private fun identifier(uriIdx: Int, nameIdx: Int): Identifier {
        val localName = lookupString(nameIdx).orEmpty()
        val uri: String?
        val prefix: String?
        if (uriIdx == -0x1) {
            uri = null
            prefix = null
        } else {
            uri = lookupString(uriIdx)
            prefix = namespaces[uri]
        }
        val qname = if (prefix.isNullOrBlank()) {
            localName
        } else {
            "$prefix:$localName"
        }
        return Identifier(
            localName, uri, prefix, qname
        )
    }

    /**
     * @param type the attribute type
     * @param data the data value
     * @return the typed value
     */
    private fun getAttributeValue(type: Int, data: Int): String? {
        val res: String?
        when (type) {
            TYPE_STRING -> res = lookupString(data)
            TYPE_DIMEN -> res = (data shr 8).toString() + DIMEN[data and 0xFF]
            TYPE_FRACTION -> {
                val fracValue = data.toDouble() / 0x7FFFFFFF.toDouble()
                res = DecimalFormat("#.##%").format(fracValue)
            }
            TYPE_FLOAT -> res = java.lang.Float.intBitsToFloat(data).toString()
            TYPE_INT, TYPE_FLAGS -> res = data.toString()
            TYPE_BOOL -> res = toString(data != 0)
            TYPE_COLOR, TYPE_COLOR2 -> res = String.format("#%08X", data)
            TYPE_ID_REF -> res = String.format("@id/0x%08X", data)
            TYPE_ATTR_REF -> res = String.format("?id/0x%08X", data)
            else -> {
                Timber.w(
                    "Unknown attribute type 0x${type.toString(16)} with data 0x${
                        data.toString(
                            16
                        )
                    }"
                )
                res = String.format("%08X/0x%08X", type, data)
            }
        }
        return res
    }

    // endregion

    companion object {
        private const val WORD_START_DOCUMENT = 0x00080003

        private const val WORD_STRING_TABLE = 0x001C0001
        private const val WORD_RES_TABLE = 0x00080180

        private const val WORD_START_NS = 0x00100100
        private const val WORD_END_NS = 0x00100101
        private const val WORD_START_TAG = 0x00100102
        private const val WORD_END_TAG = 0x00100103
        private const val WORD_TEXT = 0x00100104
        private const val WORD_EOS = -0x1
        private const val WORD_SIZE = 4

        private const val TYPE_ID_REF = 0x01000008
        private const val TYPE_ATTR_REF = 0x02000008
        private const val TYPE_STRING = 0x03000008
        private const val TYPE_DIMEN = 0x05000008
        private const val TYPE_FRACTION = 0x06000008
        private const val TYPE_INT = 0x10000008
        private const val TYPE_FLOAT = 0x04000008

        private const val TYPE_FLAGS = 0x11000008
        private const val TYPE_BOOL = 0x12000008
        private const val TYPE_COLOR = 0x1C000008
        private const val TYPE_COLOR2 = 0x1D000008

        private val DIMEN = arrayOf("px", "dp", "sp", "pt", "in", "mm")
    }
}