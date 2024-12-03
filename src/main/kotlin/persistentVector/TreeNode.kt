package persistentVector

class TreeNode<E>: Node<E> {
    val subNodes: List<Node<E>>

    constructor(vararg subNodes: Node<E>) {
        if (subNodes.size < 1 || subNodes.size > NodeLength) {
            throw IllegalArgumentException("Unexpected size of data: ${subNodes.size} - min: 1, max: $NodeLength")
        }
        this.subNodes = subNodes.asList()
    }

    private constructor(subNodes: List<Node<E>>) {
        this.subNodes = subNodes
    }

    override fun plus(value: E): Node<E> {
        if (!subNodes.last().full) {
            val newSubNodes = subNodes.subList(0, subNodes.size - 1).plus(ValueNode(value))
            return TreeNode(newSubNodes)
        }
        else {
            return plusNode(ValueNode(value))
        }
    }

    fun plusNode(newNode: Node<E>): Node<E> {
        if (subNodes.size < NodeLength) {
            return TreeNode(subNodes + newNode)
        } else {
            return TreeNode(this, TreeNode(listOf(newNode)))
        }
    }

    override val full: Boolean
        get() {
            return subNodes.size == NodeLength
                    && subNodes.last().full
        }
}
