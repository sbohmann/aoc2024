package persistentVector

class ValueNode<E> : Node<E> {
    val data: List<E>
    override val size: Int
    override val full: Boolean
    override val depth: Byte = 0

    constructor(value: E) {
        this.data = listOf(value)
        size = data.size
        full = data.size == NodeLength
    }

    constructor(data: List<E>) {
        if (data.isEmpty() || data.size > NodeLength) {
            throw IllegalArgumentException("Unexpected size of data: ${data.size} - min: 1, max: $NodeLength")
        }
        this.data = ArrayList(data)
        size = data.size
        full = data.size == NodeLength
    }

    override fun plus(value: E): Node<E> {
        if (full) {
            return TreeNode(1, this, ValueNode(value))
        } else {
            return ValueNode(data + value)
        }
    }

    override fun get(index: Int): E {
        return data[index]
    }

    override fun with(index: Int, value: E): Node<E> {
        TODO("Not yet implemented")
    }
}
