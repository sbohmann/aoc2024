package persistentVector

import kotlin.math.min

const val NodeLength = 32

class PersistentVector<E>: List<E> {
    val root: Node<E>?

    constructor() {
        root = EmptyNode()
    }

    constructor(vararg values: E) : this(values.asList()) {
    }

    constructor(values: List<E>) {
        this.root = buildRoot(values)
    }

    private fun buildRoot(values: List<E>): Node<E> {
        if (values.isEmpty()) {
            return EmptyNode()
        } else if (values.size <= NodeLength) {
            return ValueNode<E>(values)
        } else {
            var root: TreeNode<E>? = null
            for (index in 0..<values.size step NodeLength) {
                val end = min(index + 32, values.size)
                val newValueNode = ValueNode(values.subList(index, end))
                root = root?.plusNode(newValueNode)
                    ?: TreeNode(newValueNode)
            }
            return root!!
        }
    }

    override val size: Int
        get() = TODO("Not yet implemented")

    override fun get(index: Int): E {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<E> {
        TODO("Not yet implemented")
    }

    override fun listIterator(): ListIterator<E> {
        TODO("Not yet implemented")
    }

    override fun listIterator(index: Int): ListIterator<E> {
        TODO("Not yet implemented")
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<E> {
        TODO("Not yet implemented")
    }

    override fun lastIndexOf(element: E): Int {
        TODO("Not yet implemented")
    }

    override fun indexOf(element: E): Int {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(element: E): Boolean {
        TODO("Not yet implemented")
    }
}
