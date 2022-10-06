package fr.xgouchet.packageexplorer.core.utils

import fr.xgouchet.packageexplorer.core.utils.ManifestType.ATTRS
import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList

/**
 * Created by Ekrem HATİPOĞLU on 11.10.2020.
 */

object ManifestType {
    const val APPLICATION = "application"
    const val INTENT_FILTER = "intent-filter"
    const val ATTRS = "attrs"
}

data class AndroidManifest(val items: List<Item>)
data class Item(val tagName: String, val attrs: Map<String, String>, val childList: List<Item> = listOf())

fun Document.parseDocumentToManifest(): AndroidManifest {
    val items = mutableListOf<Item>()

    val applicationNode = this.documentElement.getElementsByTagName(ManifestType.APPLICATION.toString())
    applicationNode.takeFirst {
        childNodes.forEach {
            items.add(extractItem())
        }
    }

    return AndroidManifest(items)
}

fun AndroidManifest.filterByName(name: String) = this.items.firstOrNull { it.attrs.containsValue(name) }

fun List<Item>.formatItem(): List<Map<String, Map<String, String>>> {

    val list = mutableListOf<Map<String, Map<String, String>>>()

    forEach { intentFilter ->
        val item = mutableMapOf<String, Map<String, String>>()
        if (intentFilter.attrs.isNotEmpty()) {
            item[ATTRS] = intentFilter.attrs
        }

        intentFilter.childList.forEach { child ->
            item[child.tagName] = child.attrs
        }
        list.add(item)
    }

    return list
}

fun Item.getItemsFromChildByType(type: String): List<Item> {
    return childList.filter { it.tagName == type }
}

private fun Node.extractChildren(): List<Item> {
    val items = mutableListOf<Item>()
    childNodes.forEach {
        items.add(extractItem())
    }

    return items
}

private fun Node.extractAttrs(): Map<String, String> {
    val map = hashMapOf<String, String>()
    attributes?.forEach {
        map[nodeName] = nodeValue
    }
    return map
}

private fun Node.extractItem(): Item {
    val tagName = nodeName
    val attrs = extractAttrs()
    val childList = extractChildren()

    return Item(tagName, attrs, childList)
}

private fun NodeList.forEach(block: Node.() -> Unit) {
    for (i in 0 until this.length) {
        this.item(i).block()
    }
}

private fun NamedNodeMap.forEach(block: Node.() -> Unit) {
    for (i in 0 until this.length) {
        this.item(i).block()
    }
}

private fun NodeList.takeFirst(block: Node.() -> Unit) {
    if (this.length > 0)
        this.item(0).block()
}
