package persistentVector

const val NodeLength = 32

class PersistentVector<E> {
    val root: Node<E>?

    constructor() {
        root = null
    }

    constructor(vararg values: E) : this(values.asList()) {
    }

    constructor(values: List<E>) {
        var root = null
        var index = 0
        while (values.size - index >= NodeLength) {
            root = root.addValueNode(values.subList(index, index + 32))
        }

        this.root = root
    }

    private fun addValueNode(values: List<E>) {
        if (root == null) {
            //root = ValueNode(values)
        }
    }
}
