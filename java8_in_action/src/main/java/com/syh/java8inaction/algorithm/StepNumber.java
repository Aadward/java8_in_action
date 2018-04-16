package com.syh.java8inaction.algorithm;

public class StepNumber {

    public static void main(String[] args) {
        Node root = constructTree();
        LinkNode node = find(root, 5);
        while (node != null) {
            System.out.println(node.node.value);
            node = node.next;
        }

    }


    static LinkNode find(Node root, int target) {
        LinkNode current = new LinkNode(root);
        if (root.value == target) {
            return current;
        } else if (root.left == null && root.right == null) {
            return null;
        }

        if (root.left != null) {
            LinkNode tmp = find(root.left, target);
            if (tmp != null) {
                current.next = tmp;
                return current;
            }
        }

        if (root.right != null) {
            LinkNode tmp = find(root.right, target);
            if (tmp != null) {
                current.next = tmp;
                return current;
            }
        }

        return null;
    }

    static class LinkNode {
        Node node;
        LinkNode next;

        public LinkNode(Node node) {
            this.node = node;
        }
    }

    static class Node {
        Node left, right;
        int value;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     *         1
     *       2   3
     *     4  5 6  7
     */
    public static Node constructTree() {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);
        return root;
    }
}
