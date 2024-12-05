package persistentVector

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

const val vectorSize = 2_500_000

class PersistentVectorTest {
    @Test
    fun `test empty persistent vector`() {
        val vector = PersistentVector<Int>()
        assertTrue(vector.isEmpty())
        assertEquals(0, vector.size)
    }

    @Test
    fun `test persistent vector size`() {
        val vector = PersistentVector(1, 2, 3, 4, 5)
        assertFalse(vector.isEmpty())
        assertEquals(5, vector.size)
    }

    @Test
    fun `test get element by index`() {
        val vector = PersistentVector("a", "b", "c")
        assertEquals("a", vector[0])
        assertEquals("b", vector[1])
        assertEquals("c", vector[2])
    }

    @Test
    fun `test get index out of bounds`() {
        val vector = PersistentVector(10, 20)
        assertThrows(IndexOutOfBoundsException::class.java) {
            vector[2]
        }
    }

    @Test
    fun `test iterator of vector`() {
        val vector = PersistentVector(10, 20, 30)
        val iterator = vector.iterator()
        assertTrue(iterator.hasNext())
        assertEquals(10, iterator.next())
        assertEquals(20, iterator.next())
        assertEquals(30, iterator.next())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun `test list iterator of vector`() {
        val vector = PersistentVector(100, 200, 300)
        val listIterator = vector.listIterator()

        assertEquals(100, listIterator.next())
        assertEquals(200, listIterator.next())
        assertTrue(listIterator.hasPrevious())
        assertEquals(200, listIterator.previous())
        assertEquals(200, listIterator.next())
        assertTrue(listIterator.hasNext())
        assertEquals(300, listIterator.next())
        assertFalse(listIterator.hasNext())
    }

    @Test
    fun `test persistent vector with large number of elements`() {
        val elements = Array(vectorSize) { it }
        val vector = PersistentVector(elements.asList())

        assertFalse(vector.isEmpty(), "Failed: Expected vector to be not empty")
        assertEquals(vectorSize, vector.size, "Failed: Expected size to be vectorSize")

        for (i in 0 until vector.size) {
            assertEquals(elements[i], vector[i], "Failed: Element at index $i does not match")
        }
    }

    @Test
    fun `test iterator of large vector`() {
        val elements = Array(vectorSize) { it }
        val vector = PersistentVector(elements.asList())
        val iterator = vector.iterator()
        var index = 0

        while (iterator.hasNext()) {
            assertEquals(elements[index], iterator.next(), "Failed: Iterator element at index $index does not match")
            index++
        }
        assertEquals(vectorSize, index, "Failed: Iterator did not traverse all elements")
    }

    @Test
    fun `test persistent vector with large number of elements and built using +`() {
        var vector = PersistentVector<Int>()
        val elements = Array(vectorSize) { it }
        elements.forEach { vector += it }

        assertFalse(vector.isEmpty(), "Failed: Expected vector to be not empty")
        assertEquals(vectorSize, vector.size, "Failed: Expected size to be vectorSize")

        for (i in 0 until vector.size) {
            assertEquals(elements[i], vector[i], "Failed: Element at index $i does not match")
        }
    }

    @Test
    fun `test persistent vector with large number of elements and created from Iterable +`() {
        val elements = Array(vectorSize) { it }
        val vector = PersistentVector(elements.asIterable())

        assertFalse(vector.isEmpty(), "Failed: Expected vector to be not empty")
        assertEquals(vectorSize, vector.size, "Failed: Expected size to be vectorSize")

        for (i in 0 until vector.size) {
            assertEquals(elements[i], vector[i], "Failed: Element at index $i does not match")
        }
    }

    @Test
    fun `test iterator of large vector and built using +`() {
        var vector = PersistentVector<Int>()
        val elements = Array(vectorSize) { it }
        elements.forEach { vector += it }
        val iterator = vector.iterator()
        var index = 0

        while (iterator.hasNext()) {
            assertEquals(elements[index], iterator.next(), "Failed: Iterator element at index $index does not match")
            index++
        }
        assertEquals(vectorSize, index, "Failed: Iterator did not traverse all elements")
    }

    @Test
    fun `test subList operation`() {
        val vector = PersistentVector(1, 2, 3, 4, 5)
        val subVector = vector.subList(1, 4)

        assertEquals(3, subVector.size, "Failed: Expected size of sublist to be 3")
        assertEquals(2, subVector[0], "Failed: Expected first element of sublist to be 2")
        assertEquals(4, subVector[2], "Failed: Expected last element of sublist to be 4")
    }

    @Test
    fun `test lastIndexOf operation`() {
        val vector = PersistentVector(1, 2, 3, 2, 4)
        val lastIndex = vector.lastIndexOf(2)

        assertEquals(3, lastIndex, "Failed: Expected last index of 2 to be 3")
    }

    @Test
    fun `test indexOf operation`() {
        val vector = PersistentVector(1, 2, 3, 2, 4)
        val index = vector.indexOf(2)

        assertEquals(1, index, "Failed: Expected first index of 2 to be 1")
    }

    @Test
    fun `test containsAll operation`() {
        val vector = PersistentVector(1, 2, 3, 4, 5)
        val containsAllResult = vector.containsAll(listOf(2, 3, 4))

        assertTrue(containsAllResult, "Failed: Expected vector to contain all elements 2, 3, 4")
    }

    @Test
    fun `test contains operation`() {
        val vector = PersistentVector(1, 2, 3, 4, 5)
        val containsResult = vector.contains(3)

        assertTrue(containsResult, "Failed: Expected vector to contain element 3")
    }
}
