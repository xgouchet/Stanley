package fr.xgouchet.packageexplorer.parser

interface CompressedXmlParserListener {

    /**
     * Receive notification of the beginning of a document.
     */
    fun startDocument()

    /**
     * Receive notification of the end of a document.
     */
    fun endDocument()

    /**
     * Begin the scope of a prefix-URI Namespace mapping.
     *
     * @param prefix  the Namespace prefix being declared. An empty string is used
     * for the default element namespace, which has no prefix.
     * @param uri  the Namespace URI the prefix is mapped to
     */
    fun startPrefixMapping(prefix: String?, uri: String)

    /**
     * End the scope of a prefix-URI mapping.
     *
     * @param prefix the prefix that was being mapped. This is the empty string
     * when a default mapping scope ends.
     * @param uri  the Namespace URI the prefix is mapped to
     */
    fun endPrefixMapping(prefix: String?, uri: String)

    /**
     * Receive notification of the beginning of an element.
     *
     * @param identifier  the element identifier
     * @param attributes  the attributes attached to the element. If there are no
     * attributes, it shall be an empty Attributes array. The value
     * of this object after startElement returns is undefined
     */
    fun startElement(
        identifier : Identifier,
        attributes: Collection<Attribute>
    )

    /**
     * Receive notification of the end of an element.
     *
     * @param identifier  the element identifier
     */
    fun endElement(identifier : Identifier)

    /**
     * Receive notification of text.
     *
     * @param data the text data
     */
    fun text(data: String)

    /**
     * Receive notification of character data (in a `<![CDATA[ ]]>` block).
     * @param data the text data
     */
    fun characterData(data: String)

    /**
     * Receive notification of a processing instruction.
     *
     * @param target the processing instruction target
     * @param data the processing instruction data, or null if none was supplied.
     * The data does not include any whitespace separating it from
     * the target
     */
    fun processingInstruction(target: String, data: String?)
}