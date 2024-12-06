package persistentVector

class EmptyNode<E> : Node<E> {
    override fun plus(value: E): Node<E> {
        return ValueNode(value)
    }

    override val size: Int = 0

    override val full = false

    override val depth: Byte = 0

    override fun get(index: Int): E {
        throw UnsupportedOperationException()
    }

    override fun with(index: Int, value: E): Node<E> {
        TODO("Not yet implemented")
    }
}
