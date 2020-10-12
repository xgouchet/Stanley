package fr.xgouchet.packageexplorer.core.utils

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

/**
 * Created by Ekrem HATİPOĞLU on 11.10.2020.
 */

const val ACTIVITY_NODE_NAME = "activity"
const val BROADCAST_RECEIVER_NODE_NAME = "receiver"
const val SERVICE_NODE_NAME = "service"
private const val ANDROID_NAME_NODE_NAME = "android:name"
private const val INTENT_FILTER_NODE_NAME = "intent-filter"
private const val ACTION_NODE_NAME = "action"
private const val CATEGORY_NODE_NAME = "category"
private const val DATA_ANDROID_NAME = "data"

fun Document.getIntentFilters(tagName: String, nameValue: String): String{
    var result = ""
    this.documentElement.getElementsByTagAndAttr(tagName, nameValue)?.let { activityNode ->
        activityNode.childNodes.findByNodeName(INTENT_FILTER_NODE_NAME)?.childNodes?.let { intentFilters ->
            intentFilters.apply {
                result += extractActions(findAllByNodeName(ACTION_NODE_NAME)).formatList("Actions")
                result += extractCategories(findAllByNodeName(CATEGORY_NODE_NAME)).formatList("Categories")
                result += extractData(findAllByNodeName(DATA_ANDROID_NAME)).formatList("Data")
            }
        }
    }

    return result

}

private fun List<String>.formatList(title: String, separator: String = "\n\t●\t"): String{
    var result = ""

    if (this.isNotEmpty()){
        result +=  "\n\n\t$title:${this.joinToString(separator = separator, prefix = separator)}"
    }

    return result
}
private fun Element.getElementsByTagAndAttr(tagName: String, nameValue: String): Node? {
    return this.getElementsByTagName(tagName).findByAttr(ANDROID_NAME_NODE_NAME, nameValue)
}

private fun extractData(nodeList: List<Node>) = nodeList.mapNotNull { it.getAttrNodeValue() }

private fun extractCategories(nodeList: List<Node>) = nodeList.mapNotNull { it.getAttrNodeValue() }

private fun extractActions(nodeList: List<Node>) = nodeList.mapNotNull { it.getAttrNodeValue() }

private fun NodeList.findByAttr(name: String, value: String): Node? {
    for (i in 0 until this.length){
        val element = this.item(i)
        element?.findByAttr(name, value)?.let {
            return element
        }
    }

    return null
}

private fun Node.findByAttr(name: String, value: String): Node? {
    val attributes = this.attributes
    for (i in 0 until attributes.length){
        val node = attributes.item(i)
        if (node.nodeName == name && node.nodeValue == value){
            return node
        }
    }
    return null
}


private fun NodeList.findByNodeName(name: String): Node? {
    for (i in 0 until this.length){
        val node = this.item(i)
        if (node.nodeName == name){
            return node
        }
    }
    return null
}

private fun NodeList.findAllByNodeName(name: String): List<Node> {
    val list = mutableListOf<Node>()
    for (i in 0 until this.length){
        val node = this.item(i)
        if (node.nodeName == name){
            list.add(node)
        }
    }
    return list
}

private fun Node.getAttrNodeValue(index: Int = 0): String? {
    return this.attributes.item(index).nodeValue
}