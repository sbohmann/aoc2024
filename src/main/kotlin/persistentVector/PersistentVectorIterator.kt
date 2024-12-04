package persistentVector

class PersistentVectorIterator<E>(
    private val rootNode: Node<E>,
    initialIndex: Int = 0
) : ListIterator<E> {
    private var currentNode: Node<E>
    private val size = rootNode.size
    private var currentIndex: Int
    private var currentNodeIndex: Int

    init {
        currentNode = nodeForIndex(initialIndex)
        if (initialIndex == 0) {
            currentIndex = 0
            currentNodeIndex = 0
        } else {
            val valuesPerSubNode = NodeLength shl (NodeLengthInBits * rootNode.depth)
            this.currentIndex = initialIndex
            currentNodeIndex = currentIndex / valuesPerSubNode
            currentIndex = initialIndex
        }
    }

    private fun nodeForIndex(index: Int): Node<E> {
        var node = rootNode
        while (node is TreeNode) {
            val subNodeIndex = index shr (NodeLengthInBits * node.depth)
            node = node.subNodes[index]
        }
        return node
    }

    override fun next(): E {
        if (currentIndex >= size - 1) {
            throw IndexOutOfBoundsException("index: $currentIndex, size: $size")
        }
        ++currentIndex
        val indexInsideNode = currentIndex % NodeLength
        if (indexInsideNode == 0) {
            currentNode = nodeForIndex(currentIndex)
        }
        if (currentNode is ValueNode) {
            return (currentNode as ValueNode<E>).data[indexInsideNode]
        } else {
            throw IllegalStateException("Logical error: currentNode is not a value node")
        }
    }

    override fun hasNext(): Boolean = currentIndex < size - 1

    override fun previous(): E {
        if (currentIndex <= 0) {
            throw IndexOutOfBoundsException("index: $currentIndex, size: $size")
        }
        --currentIndex
        val indexInsideNode = currentIndex % NodeLength
        if (indexInsideNode == NodeLength - 1) {
            currentNode = nodeForIndex(currentIndex)
        }
        if (currentNode is ValueNode) {
            return (currentNode as ValueNode<E>).data[indexInsideNode]
        } else {
            throw IllegalStateException("Logical error: currentNode is not a value node")
        }
    }

    override fun hasPrevious(): Boolean = currentIndex > 0

    override fun nextIndex(): Int {
        if (currentIndex >= size - 1) {
            throw IndexOutOfBoundsException("index: $currentIndex, size: $size")
        }
        return currentIndex + 1
    }

    override fun previousIndex(): Int {
        if (currentIndex <= 0) {
            throw IndexOutOfBoundsException("index: $currentIndex, size: $size")
        }
        return currentIndex - 1
    }
}
