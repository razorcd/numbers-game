package com.challenge.application.utils;

import com.challenge.application.utils.collections.CircularLinkedList;
import com.challenge.application.utils.collections.LinkedNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CircularLinkedListTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotBuildALinkedListOfNone() {
        CircularLinkedList<Object> circularLinkedList = new CircularLinkedList<>();
        LinkedNode<Object> linkedNode = circularLinkedList.getFirst();
    }

    @Test
    public void shouldBuildALinkedListOfOne() {
        Object o = new Object();
        CircularLinkedList<Object> circularLinkedList = new CircularLinkedList<>(o);
        LinkedNode<Object> linkedNode = circularLinkedList.getFirst();

        assertEquals("CircularLinkedList of 1 object should make first link hold the object.", o, linkedNode.getValue());
        assertEquals("CircularLinkedList of 1 object should make first link same as next.", linkedNode, linkedNode.getNext());
    }

    @Test
    public void shouldBuildALinkedListOfTwo() {
        Object o1 = new Object();
        Object o2 = new Object();
        CircularLinkedList<Object> circularLinkedList = new CircularLinkedList<>(o1, o2);
        LinkedNode<Object> linkedNode = circularLinkedList.getFirst();

        assertEquals("CircularLinkedList of 2 objects should make 1st link hold the object1.", o1, linkedNode.getValue());
        assertEquals("CircularLinkedList of 2 objects should make 2nd link hold the object2.", o2, linkedNode.getNext().getValue());
        assertEquals("CircularLinkedList of 2 objects should be circular.", linkedNode, linkedNode.getNext().getNext());
    }

}