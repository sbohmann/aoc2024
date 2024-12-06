package persistentVector

interface Node<E> {
    val size: Int

    val full: Boolean

    val depth: Byte

    fun plus(value: E): Node<E>

    fun get(index: Int): E

    fun with(index: Int, value: E): Node<E>
}
