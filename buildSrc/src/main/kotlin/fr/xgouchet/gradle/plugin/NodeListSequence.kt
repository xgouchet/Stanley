package fr.xgouchet.gradle.plugin

import org.w3c.dom.Node
import org.w3c.dom.NodeList

class NodeListSequence(
    private val nodeList: NodeList
) : Sequence<Node> {

    override fun iterator(): kotlin.collections.Iterator<Node> {
        return Iterator(nodeList)
    }

    class Iterator(
        private val nodeList: NodeList
    ) : kotlin.collections.Iterator<Node> {
        private var i = 0
        override fun hasNext() = nodeList.length > i
        override fun next(): Node = nodeList.item(i++)
    }
}

fun NodeList.asSequence() = NodeListSequence(this)
