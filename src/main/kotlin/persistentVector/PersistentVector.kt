package persistentVector

import kotlin.math.min

const val NodeLengthInBits = 5
const val NodeLength = (1 shl NodeLengthInBits)

class PersistentVector<E> : AbstractList<E> {
    val root: Node<E>
    override val size: Int

    constructor() {
        root = EmptyNode()
        size = root.size
    }

    constructor(vararg values: E) : this(values.asList())

    constructor(source: Iterable<E>) {
        var root: Node<E> = EmptyNode()
        source.forEach { root = root.plus(it) }
        this.root = root
        size = root.size
    }

    private constructor(root: Node<E>) {
        this.root = root
        size = root.size
    }

    constructor(values: List<E>) {
        this.root = buildRoot(values)
        size = root.size
    }

    private fun buildRoot(values: List<E>): Node<E> {
        if (values.isEmpty()) {
            return EmptyNode()
        } else if (values.size <= NodeLength) {
            return ValueNode(values)
        } else {
            var root: TreeNode<E>? = null
            for (index in 0..<values.size step NodeLength) {
                val end = min(index + NodeLength, values.size)
                val newValueNode = ValueNode(values.subList(index, end))
                root = root?.plusNode(newValueNode)
                    ?: TreeNode(1, newValueNode)
            }
            return root!!
        }
    }

    override fun get(index: Int): E {
        if (index >= size) {
            throw IndexOutOfBoundsException("index: $index, $size: 0")
        }
        return root.get(index)
    }

    fun with(index: Int, value: E): PersistentVector<E> {
        return PersistentVector(root = root.with(index, value))
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    operator fun plus(value: E): PersistentVector<E> {
        return PersistentVector(root.plus(value))
    }

    override fun iterator(): Iterator<E> {
        return PersistentVectorIterator(root)
    }

    override fun listIterator(): ListIterator<E> {
        return PersistentVectorIterator(root)
    }

    override fun listIterator(index: Int): ListIterator<E> {
        return PersistentVectorIterator(root, index)
    }
}

fun main() {
    val iterator = PersistentVector(1, 2, 3).listIterator()
    while (iterator.hasNext()) {
        println(iterator.next())
    }
    while (iterator.hasPrevious()) {
        println(iterator.previous())
    }
}
