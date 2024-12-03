package persistentVector

interface Node<E> {
    fun plus(value: E): Node<E>

    val full: Boolean
}
