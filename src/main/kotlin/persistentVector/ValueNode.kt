package persistentVector

class ValueNode<E> : Node<E> {
    val data: List<E>

    constructor(value: E) {
        this.data = listOf(value)
    }

    constructor(data: List<E>) {
        if (data.isEmpty() || data.size > NodeLength) {
            throw IllegalArgumentException("Unexpected size of data: ${data.size} - min: 1, max: $NodeLength")
        }
        this.data = ArrayList(data)
    }

    override val size: Int
        get() {
            return data.size
        }

    override fun plus(value: E): Node<E> {
        if (full) {
            return TreeNode(1, this, ValueNode(value))
        } else {
            return ValueNode(data + value)
        }
    }

    override val full: Boolean
        get() {
            return data.size == NodeLength
        }

    override val depth: Byte = 0

    override fun get(index: Int): E {
        return data[size]
    }
}
