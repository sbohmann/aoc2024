class GeneratedPersistentVector<E> private constructor(
    private val root: Node<E>,
    val size: Int
) {

    private sealed class Node<E> {
        class Branch<E>(val children: List<Node<E>>) : Node<E>()
        class Leaf<E>(val elements: List<E>) : Node<E>()
    }

    constructor() : this(Node.Leaf(emptyList()), 0)

    fun add(element: E): GeneratedPersistentVector<E> {
        val (newRoot, _) = addElement(root, element, size, 0)
        return GeneratedPersistentVector(newRoot, size + 1)
    }

    private fun addElement(node: Node<E>, element: E, index: Int, depth: Int): Pair<Node<E>, Boolean> {
        if (node is Node.Leaf) {
            return if (node.elements.size < MAX_ELEMENTS) {
                Node.Leaf(node.elements + element) to false
            } else {
                val splitPoint = node.elements.size / 2
                val newBranch = Node.Branch(
                    listOf(
                        Node.Leaf(node.elements.subList(0, splitPoint)),
                        Node.Leaf(node.elements.subList(splitPoint, node.elements.size) + element)
                    )
                )
                newBranch to true
            }
        } else if (node is Node.Branch) {
            val childIndex = (index shr (depth * BRANCH_BITS)) and (BRANCH_FACTOR - 1)
            val (newChild, overflow) = addElement(node.children[childIndex], element, index, depth + 1)
            val newChildren = node.children.toMutableList().apply {
                this[childIndex] = newChild
                if (overflow && size.shr(MAX_DEPTH * BRANCH_BITS) > 0) {
                    add(Node.Leaf(emptyList()))
                }
            }
            return Node.Branch(newChildren) to false
        } else {
            throw IllegalStateException("Unsupported node type: $node")
        }
    }

    fun get(index: Int): E {
        var currentNode = root
        var depth = 0
        while (currentNode is Node.Branch) {
            val childIndex = (index shr (depth * BRANCH_BITS)) and (BRANCH_FACTOR - 1)
            currentNode = currentNode.children[childIndex]
            depth++
        }
        if (currentNode is Node.Leaf) {
            return currentNode.elements[index - ((1 shl (depth * BRANCH_BITS)) - 1)]
        } else {
            throw IndexOutOfBoundsException()
        }
    }

    companion object {
        private const val BRANCH_BITS = 5
        private const val BRANCH_FACTOR = 1 shl BRANCH_BITS
        private const val MAX_ELEMENTS = BRANCH_FACTOR
        private const val MAX_DEPTH = 5
    }
}

fun main() {
    val vector = GeneratedPersistentVector<String>()
    val vector1 = vector.add("Hello")
    val vector2 = vector1.add("World")

    println("Vector size: ${vector.size}")       // Vector size: 0
    println("Vector1 size: ${vector1.size}")     // Vector1 size: 1
    println("Vector2 size: ${vector2.size}")     // Vector2 size: 2

    println("First element in vector2: ${vector2.get(0)}")  // First element in vector2: Hello
    println("Second element in vector2: ${vector2.get(1)}") // Second element in vector2: World
}