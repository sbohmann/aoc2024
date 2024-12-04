package persistentVector

class TreeNode<E> : Node<E> {
    val subNodes: List<Node<E>>
    override val size: Int
    override val depth: Byte
    override val full: Boolean

    constructor(depth: Byte, vararg subNodes: Node<E>) {
        if (subNodes.isEmpty() || subNodes.size > NodeLength) {
            throw IllegalArgumentException("Unexpected size of data: ${subNodes.size} - min: 1, max: $NodeLength")
        }
        this.subNodes = subNodes.asList()
        size = calculateSize()
        this.depth = depth
        full = subNodes.size == NodeLength
                && subNodes.last().full
    }

    private constructor(depth: Byte, subNodes: List<Node<E>>) {
        this.subNodes = subNodes
        this.size = calculateSize()
        this.depth = depth
        full = subNodes.size == NodeLength
                && subNodes.last().full
    }

    private fun calculateSize() = this.subNodes.fold(0) { sum, next -> sum + next.size }

    override fun plus(value: E): Node<E> {
        if (full) {
            return TreeNode((depth + 1).toByte(), this, nodeAtDepth(ValueNode(value), depth))
        } else if (subNodes.last().full) {
            return plusNode(ValueNode(value))
        } else {
            val newSubNodes = subNodes.subList(0, subNodes.size - 1).plus(subNodes.last().plus(value))
            return TreeNode(depth, newSubNodes)
        }
    }

    fun plusNode(newNode: Node<E>): TreeNode<E> {
        if (full) {
            return TreeNode((depth + 1).toByte(), this, nodeAtDepth(newNode, depth))
        }
        val lastSubNode = subNodes.last()
        if (lastSubNode is TreeNode) {
            if (lastSubNode.full) {
                return TreeNode(depth, subNodes.plus(lastSubNode.plusNode(nodeAtDepth(newNode, (depth - 1).toByte()))))
            } else {
                return TreeNode(depth, subNodes.subList(0, subNodes.size - 1).plus(nodeAtDepth(lastSubNode.plusNode(newNode), (depth - 1).toByte())))
            }
        } else {
            if (!lastSubNode.full) {
                throw IllegalStateException("Logical error: last tree sub node is not full")
            }
            return TreeNode(depth, subNodes.plus(newNode))
        }
    }

    fun nodeAtDepth(node: Node<E>, requiredDepth: Byte): Node<E> {
        if (node.depth == requiredDepth) {
            return node
        } else {
            return TreeNode(requiredDepth, nodeAtDepth(node, (requiredDepth - 1).toByte()))
        }
    }

    override fun get(index: Int): E {
        val valuesPerSubNode = NodeLength shl (NodeLengthInBits * (depth - 1))
        val subNodeIndex = index / valuesPerSubNode
        val indexForSubNode = index % valuesPerSubNode
        return subNodes[subNodeIndex].get(indexForSubNode)
    }
}
