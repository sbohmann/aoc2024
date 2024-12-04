package persistentVector

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PersistentVectorLargeTest {
    @Test
    fun `test persistent vector with large number of elements`() {
        val elements = Array(25000) { it }
        val vector = PersistentVector(*elements)

        assertFalse(vector.isEmpty(), "Failed: Expected vector to be not empty")
        assertEquals(25000, vector.size, "Failed: Expected size to be 25000")

        for (i in 0 until vector.size) {
            assertEquals(elements[i], vector[i], "Failed: Element at index $i does not match")
        }
    }

    @Test
    fun `test iterator of large vector`() {
        val elements = Array(25000) { it }
        val vector = PersistentVector(*elements)
        val iterator = vector.iterator()
        var index = 0

        while (iterator.hasNext()) {
            assertEquals(elements[index], iterator.next(), "Failed: Iterator element at index $index does not match")
            index++
        }
        assertEquals(25000, index, "Failed: Iterator did not traverse all elements")
    }
}
