package com.syh.java8inaction.algorithm;

public class InvertLinkedList {

    public static void main(String[] args) {
        Node node = generateList(5);
        print(node);
        System.out.println("====invert====");
        print(invert(node));
    }

    static Node invert(Node node) {
        Node head = new Node(0);
        while (node != null) {
            Node next = node.next;
            node.next = head.next;
            head.next = node;
            node = next;
        }
        return head.next;
    }


    static class Node {
        int i;
        Node next;

        public Node(int i) {
            this.i = i;
        }
    }

    static Node generateList(int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("length should greater than 0");
        }

        int i = 1;
        Node first = new Node(i++);
        Node pointer = first;

        while (--len > 0) {
            pointer.next = new Node(i++);
            pointer = pointer.next;
        }

        return first;
    }

    static void print(Node node) {
        while (node != null) {
            System.out.println(node.i);
            node = node.next;
        }
    }

}
