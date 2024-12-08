package persistentLinkedList

data class PersistentLinkedList<E>(val value: E, val next: PersistentLinkedList<E>?): AbstractList<E>() {
    override val size: Int = 1 + (next?.size ?: 0)

    override fun get(index: Int): E {
        var runningIndex = index
        var nextList = next
        while (runningIndex > 0) {
            if (nextList != null) {
                nextList = nextList.next
            } else {
                throw IndexOutOfBoundsException("Index: $index, size: $size")
            }
            --runningIndex
            nextList = nextList?.next
        }
        return nextList!!.value
    }

    override fun iterator(): Iterator<E> {
        return object: Iterator<E> {
            var current: PersistentLinkedList<E>? = this@PersistentLinkedList

            override fun hasNext(): Boolean {
                return current != null
            }

            override fun next(): E {
                if (current == null) {
                    throw NoSuchElementException()
                }
                val result = current!!.value
                current = current!!.next
                return result
            }
        }
    }
}

fun <E>cons(value: E, next: PersistentLinkedList<E>): PersistentLinkedList<E> {
    return PersistentLinkedList(value, next)
}
