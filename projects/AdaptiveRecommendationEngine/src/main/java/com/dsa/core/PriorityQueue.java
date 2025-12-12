package com.dsa.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Custom Min-Heap Priority Queue implementation.
 * @param <T> The type of elements held in this queue.
 */
public class PriorityQueue<T> {
    private List<T> heap;
    private Comparator<T> comparator;

    public PriorityQueue(Comparator<T> comparator) {
        this.heap = new ArrayList<>();
        this.comparator = comparator;
    }

    public void offer(T element) {
        heap.add(element);
        siftUp(heap.size() - 1);
    }

    public T poll() {
        if (isEmpty()) return null;
        T result = heap.get(0);
        T last = heap.remove(heap.size() - 1);
        if (!isEmpty()) {
            heap.set(0, last);
            siftDown(0);
        }
        return result;
    }

    public T peek() {
        if (isEmpty()) return null;
        return heap.get(0);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    private void siftUp(int index) {
        T element = heap.get(index);
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            T parent = heap.get(parentIndex);
            if (comparator.compare(element, parent) >= 0) {
                break;
            }
            heap.set(index, parent);
            index = parentIndex;
        }
        heap.set(index, element);
    }

    private void siftDown(int index) {
        T element = heap.get(index);
        int half = heap.size() / 2;
        while (index < half) {
            int childIndex = 2 * index + 1;
            T child = heap.get(childIndex);
            int rightChildIndex = childIndex + 1;
            if (rightChildIndex < heap.size() &&
                    comparator.compare(heap.get(rightChildIndex), child) < 0) {
                childIndex = rightChildIndex;
                child = heap.get(childIndex);
            }
            if (comparator.compare(element, child) <= 0) {
                break;
            }
            heap.set(index, child);
            index = childIndex;
        }
        heap.set(index, element);
    }
}
