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
        return value
    }

    override fun iterator(): Iterator<E> {
        return object: Iterator<E> {
            var start: PersistentLinkedList<E>? = this@PersistentLinkedList

            override fun hasNext(): Boolean {
                return start != null
            }

            override fun next(): E {
                if (start == null) {
                    throw NoSuchElementException()
                }
                val result = start!!.value
                start = start!!.next
                return result
            }
        }
    }
}

fun <E>cons(value: E, next: PersistentLinkedList<E>): PersistentLinkedList<E> {
    return PersistentLinkedList(value, next)
}
