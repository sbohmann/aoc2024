package persistentLinkedList;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JavaPersistentLinkedList<E> extends AbstractList<E> {
    private final E value;
    private final JavaPersistentLinkedList<E> next;
    private final int size;

    public JavaPersistentLinkedList(E value) {
        this.value = value;
        this.next = null;
        this.size = 1;
    }

    public JavaPersistentLinkedList(E value, JavaPersistentLinkedList<E> next) {
        Objects.requireNonNull(next);
        this.value = value;
        this.next = next;
        this.size = 1 + next.size;
    }

    public int size() {
        return size;
    }

    public E get(int index) {
        var runningIndex = index;
        var nextList = next;
        while (runningIndex > 0) {
            if (nextList != null) {
                nextList = nextList.next;
            } else {
                throw new IndexOutOfBoundsException("Index: $index, size: $size");
            }
            --runningIndex;
            nextList = Objects.requireNonNull(nextList).next;
        }
        return Objects.requireNonNull(nextList).value;
    }

    @NotNull
    public Iterator<E> iterator() {
        return new Iterator<>() {
            JavaPersistentLinkedList<E> start = JavaPersistentLinkedList.this;

            public boolean hasNext() {
                return start != null;
            }

            public E next() {
                if (start == null) {
                    throw new NoSuchElementException();
                }
                var result = start.value;
                start = start.next;
                return result;
            }
        };
    }

    public static <E> PersistentLinkedList<E> cons(E value, PersistentLinkedList<E> next) {
        return new PersistentLinkedList<>(value, next);
    }
}
