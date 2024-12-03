package persistentVector

class ValueNode<E> : Node<E> {
    val data: List<E>

    constructor(value: E) {
        this.data = listOf(value)
    }

    constructor(data: List<E>) {
        if (data.size < 1 || data.size > NodeLength) {
            throw IllegalArgumentException("Unexpected size of data: ${data.size} - min: 1, max: ${NodeLength}")
        }
        this.data = ArrayList(data)
    }

    override fun plus(value: E): Node<E> {
        // TODO
        return this
    }

    override val full: Boolean
        get() {
            return data.size == NodeLength
        }
}
