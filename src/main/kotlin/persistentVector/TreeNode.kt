package persistentVector

const val debug: Boolean = true

class TreeNode<E> : Node<E> {
    val subNodes: List<Node<E>>
    override val size: Int
    override val depth: Byte
    override val full: Boolean

    constructor(depth: Byte, vararg subNodes: Node<E>): this(depth, subNodes.asList())

    private constructor(depth: Byte, subNodes: List<Node<E>>) {
        this.subNodes = subNodes
        this.size = calculateSize()
        this.depth = depth
        full = subNodes.size == NodeLength
                && subNodes.last().full
        if (debug) {
            check()
        }
    }

    private fun check() {
        for (index in 0..<subNodes.size - 1) {
            val subNode = subNodes[index]
            if (!subNode.full) {
                throw IllegalStateException("Unexpected non-full sub node at index $index of ${subNodes.size}")
            }
            checkSubNode(subNode)
        }
        checkSubNode(subNodes.last())
    }

    private fun checkSubNode(subNode: Node<E>) {
        if (subNode.depth.toInt() != depth - 1) {
            throw IllegalStateException("Unexpected sub node depth ${subNode.depth} - expected: ${depth - 1}")
        }
        if (subNode.depth.toInt() == 0 && subNode is TreeNode) {
            throw IllegalStateException("Unexpected tree node at depth 0")
        }
        if (subNode is TreeNode) {
            subNode.check()
        }
    }

    private fun calculateSize() = this.subNodes.fold(0) { sum, next -> sum + next.size }

    override fun plus(value: E): Node<E> {
        if (full) {
            return TreeNode((depth + 1).toByte(), this, nodeAtDepth(ValueNode(value), depth))
        } else if (subNodes.last().full) {
            return plusNode(ValueNode(value))
        } else {
            val newSubNodes = subNodes.subList(0, subNodes.size - 1) + subNodes.last().plus(value)
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
                return TreeNode(depth, subNodes + nodeAtDepth(newNode, (depth - 1).toByte()))
            } else {
                return TreeNode(depth, subNodes.subList(0, subNodes.size - 1) + lastSubNode.plusNode(newNode))
            }
        } else {
            if (!lastSubNode.full) {
                throw IllegalStateException("Logical error: last tree sub node is not full")
            }
            return TreeNode(depth, subNodes + newNode)
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

    override fun with(index: Int, value: E): Node<E> {
        TODO("Not yet implemented")
    }
}
