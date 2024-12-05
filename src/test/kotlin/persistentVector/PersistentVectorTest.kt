package persistentVector

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

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
        val elements = Array(250000) { it }
        val vector = PersistentVector(elements.asList())

        assertFalse(vector.isEmpty(), "Failed: Expected vector to be not empty")
        assertEquals(250000, vector.size, "Failed: Expected size to be 250000")

        for (i in 0 until vector.size) {
            assertEquals(elements[i], vector[i], "Failed: Element at index $i does not match")
        }
    }

    @Test
    fun `test iterator of large vector`() {
        val elements = Array(250000) { it }
        val vector = PersistentVector(elements.asList())
        val iterator = vector.iterator()
        var index = 0

        while (iterator.hasNext()) {
            assertEquals(elements[index], iterator.next(), "Failed: Iterator element at index $index does not match")
            index++
        }
        assertEquals(250000, index, "Failed: Iterator did not traverse all elements")
    }

    @Test
    fun `test persistent vector with large number of elements and built using +`() {
        var vector = PersistentVector<Int>()
        val elements = Array(250000) { it }
        elements.forEach { vector += it }

        assertFalse(vector.isEmpty(), "Failed: Expected vector to be not empty")
        assertEquals(250000, vector.size, "Failed: Expected size to be 250000")

        for (i in 0 until vector.size) {
            assertEquals(elements[i], vector[i], "Failed: Element at index $i does not match")
        }
    }

    @Test
    fun `test iterator of large vector and built using +`() {
        var vector = PersistentVector<Int>()
        val elements = Array(250000) { it }
        elements.forEach { vector += it }
        val iterator = vector.iterator()
        var index = 0

        while (iterator.hasNext()) {
            assertEquals(elements[index], iterator.next(), "Failed: Iterator element at index $index does not match")
            index++
        }
        assertEquals(250000, index, "Failed: Iterator did not traverse all elements")
    }
}
