package persistentVector

interface Node<E> {
    val size: Int

    val full: Boolean

    fun plus(value: E): Node<E>
}
