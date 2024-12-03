package persistentVector

class TreeNode<E> : Node<E> {
    val subNodes: List<Node<E>>
    override val size: Int
    override val depth: Byte

    constructor(depth: Byte, vararg subNodes: Node<E>) {
        if (subNodes.isEmpty() || subNodes.size > NodeLength) {
            throw IllegalArgumentException("Unexpected size of data: ${subNodes.size} - min: 1, max: $NodeLength")
        }
        this.subNodes = subNodes.asList()
        size = calculateSize()
        this.depth = depth
    }

    private constructor(depth: Byte, subNodes: List<Node<E>>) {
        this.subNodes = subNodes
        this.size = calculateSize()
        this.depth = depth
    }

    private fun calculateSize() = this.subNodes.fold(0) { sum, next -> sum + next.size }

    override fun plus(value: E): Node<E> {
        if (!subNodes.last().full) {
            val newSubNodes = subNodes.subList(0, subNodes.size - 1).plus(ValueNode(value))
            return TreeNode(depth, newSubNodes)
        } else {
            return plusNode(ValueNode(value))
        }
    }

    fun plusNode(newNode: Node<E>): TreeNode<E> {
        if (subNodes.size < NodeLength) {
            return TreeNode(depth, subNodes + newNode)
        } else {
            return TreeNode((depth + 1).toByte(), this, TreeNode(depth, listOf(newNode)))
        }
    }

    override val full: Boolean
        get() {
            return subNodes.size == NodeLength
                    && subNodes.last().full
        }

    override fun get(index: Int): E {
        val valuesPerSubNode = NodeLength shl depth.toInt()
        val subNodeIndex = index / valuesPerSubNode
        val indexForSubNode = subNodeIndex.mod(valuesPerSubNode)
        return subNodes[subNodeIndex].get(indexForSubNode)
    }
}
