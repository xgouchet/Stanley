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

enum class IntentFilterType(private val nodeName: String) {
    ACTION("action"),
    CATEGORY("category"),
    DATA("data");

    override fun toString(): String {
        return this.nodeName
    }
}

fun Document.getIntentFiltersByType(type: IntentFilterType, tagName: String, nameValue: String): List<String>{
    this.documentElement.getElementsByTagAndAttr(tagName, nameValue)?.let { activityNode ->
        activityNode.childNodes.findByNodeName(INTENT_FILTER_NODE_NAME)?.childNodes?.let { intentFilters ->
            return intentFilters.findAllByNodeName(type.toString()).mapNotNull { it.getAttrNodeValue() }
        }
    }
    return listOf()
}

private fun Element.getElementsByTagAndAttr(tagName: String, nameValue: String): Node? {
    return this.getElementsByTagName(tagName).findByAttr(ANDROID_NAME_NODE_NAME, nameValue)
}

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