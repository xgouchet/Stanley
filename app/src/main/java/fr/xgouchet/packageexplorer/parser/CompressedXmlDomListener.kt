package fr.xgouchet.packageexplorer.parser

import org.w3c.dom.Document
import org.w3c.dom.Node
import java.util.Stack
import javax.xml.parsers.DocumentBuilderFactory

class CompressedXmlDomListener : CompressedXmlParserListener {

    private val stack: Stack<Node> = Stack()
    private val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    private lateinit var document: Document

    fun getXmlDocument(): Document = document

    // region CompressedXmlParserListener

    override fun startDocument() {
        document = documentBuilder.newDocument()
        stack.push(document)
    }

    override fun endDocument() {
    }

    override fun startPrefixMapping(prefix: String?, uri: String) {
    }

    override fun endPrefixMapping(prefix: String?, uri: String) {
    }

    override fun startElement(identifier: Identifier, attributes: Collection<Attribute>) {
        val element = if (identifier.namespaceUri == null) {
            document.createElement(identifier.localName)
        } else {
            document.createElementNS(identifier.namespaceUri, identifier.qualifiedName)
        }

        for (attr: Attribute in attributes) {
            if (attr.identifier.namespaceUri == null) {
                if (attr.identifier.localName.isNotBlank()) {
                    element.setAttribute(attr.identifier.localName, attr.value)
                }
            } else {
                element.setAttributeNS(
                    attr.identifier.namespaceUri,
                    attr.identifier.qualifiedName,
                    attr.value
                )
            }
        }

        stack.peek().appendChild(element)
        stack.push(element)
    }

    override fun endElement(identifier: Identifier) {
        stack.pop()
    }

    override fun text(data: String) {
        stack.peek().appendChild(document.createTextNode(data))
    }

    override fun characterData(data: String) {
        stack.peek().appendChild(document.createCDATASection(data))
    }

    override fun processingInstruction(target: String, data: String?) {
    }

    // endregion
}
