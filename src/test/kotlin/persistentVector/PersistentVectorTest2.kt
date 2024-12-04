package persistentVector

fun main() {
    testEmptyPersistentVector()
    testPersistentVectorSize()
    testGetElementByIndex()
    testGetIndexOutOfBounds()
    testIteratorOfVector()
    testListIteratorOfVector()
}

fun testEmptyPersistentVector() {
    val vector = PersistentVector<Int>()
    assert(vector.isEmpty()) { "Failed: Expected vector to be empty" }
    assert(vector.size == 0) { "Failed: Expected size to be 0" }
}

fun testPersistentVectorSize() {
    val vector = PersistentVector(1, 2, 3, 4, 5)
    assert(!vector.isEmpty()) { "Failed: Expected vector to be not empty" }
    assert(vector.size == 5) { "Failed: Expected size to be 5" }
}

fun testGetElementByIndex() {
    val vector = PersistentVector("a", "b", "c")
    assert(vector[0] == "a") { "Failed: Expected first element to be 'a'" }
    assert(vector[1] == "b") { "Failed: Expected second element to be 'b'" }
    assert(vector[2] == "c") { "Failed: Expected third element to be 'c'" }
}

fun testGetIndexOutOfBounds() {
    val vector = PersistentVector(10, 20)
    try {
        vector[2]
        assert(false) { "Failed: Expected IndexOutOfBoundsException" }
    } catch (e: IndexOutOfBoundsException) {
        // Success
    }
}

fun testIteratorOfVector() {
    val vector = PersistentVector(10, 20, 30)
    val iterator = vector.iterator()
    assert(iterator.hasNext()) { "Failed: Expected iterator to have next element" }
    assert(iterator.next() == 10) { "Failed: Expected first element to be 10" }
    assert(iterator.next() == 20) { "Failed: Expected second element to be 20" }
    assert(iterator.next() == 30) { "Failed: Expected third element to be 30" }
    assert(!iterator.hasNext()) { "Failed: Expected iterator to not have next element" }
}

fun testListIteratorOfVector() {
    val vector = PersistentVector(100, 200, 300)
    val listIterator = vector.listIterator()

    assert(listIterator.next() == 100) { "Failed: Expected first element to be 100" }
    assert(listIterator.next() == 200) { "Failed: Expected second element to be 200" }
    assert(listIterator.hasPrevious()) { "Failed: Expected list iterator to have previous element" }
    assert(listIterator.previous() == 200) { "Failed: Expected previous element to be 200" }
    assert(listIterator.next() == 300) { "Failed: Expected third element to be 300" }
    assert(!listIterator.hasNext()) { "Failed: Expected list iterator to not have next element" }
}