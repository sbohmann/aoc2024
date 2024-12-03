package persistentVector

class EmptyNode<E> : Node<E> {
    override fun plus(value: E): Node<E> {
        return ValueNode(value)
    }

    override val full = false
}
