package com.challenge.application.utils.collections;

public class CircularLinkedList<T> {

    private LinkedNode<T> first;
    private LinkedNode<T> last;

    /**
     * Build a circular link list based on the specified objects.
     *
     * @param objects the objects to be added in each link.
     */
    public CircularLinkedList(final T... objects) {
        if (objects.length < 1) throw new IllegalArgumentException("Can not build CircularLinkedList of zero elements.");
        this.first = buildLinkListRecursive(objects, 0);
        this.last.setNext(this.first);
    }

    /**
     * Get first link;
     *
     * @return {@code [LinkedNode<T>]} first link.
     */
    public LinkedNode<T> getFirst() {
        return first;
    }

    /**
     * Builds a linear linked list of the objects.
     *
     * @param pos position to start
     * @return {@code [LinkedNode<T>]} first link of the linked list.
     */
    private LinkedNode<T> buildLinkListRecursive(final T[] objects, final int pos) {
        if (pos == objects.length-1) {
            this.last = new LinkedNode<>(objects[pos], null);
            return this.last;
        }
        return new LinkedNode<>(objects[pos], buildLinkListRecursive(objects, pos+1));
    }
}
