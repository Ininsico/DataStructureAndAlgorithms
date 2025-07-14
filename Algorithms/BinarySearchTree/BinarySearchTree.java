package BinarySearchTree;

import java.util.Scanner;

public class BinarySearchTree {
    private static class Node {
        int data;
        Node left;
        Node right;

        public Node(int data) {
            this.data = data;
            this.right = null;
            this.left = null;
        }
    }

    static class BST {
        private Node root;

        public BST() {
            this.root = null;
        }

        // INSERT
        public void insert(int data) {
            root = insertRec(root, data);
        }

        private Node insertRec(Node current, int data) {
            if (current == null) {
                return new Node(data);
            }
            if (data < current.data) {
                current.left = insertRec(current.left, data);
            } else if (data > current.data) {
                current.right = insertRec(current.right, data);
            }
            return current;
        }

        // SEARCH
        public boolean contains(int data) {
            return containsRec(root, data);
        }

        private boolean containsRec(Node current, int data) {
            if (current == null)
                return false;
            if (data == current.data)
                return true;
            return data < current.data
                    ? containsRec(current.left, data)
                    : containsRec(current.right, data);
        }

        // DELETE
        public void delete(int data) {
            root = deleteRec(root, data);
        }

        private Node deleteRec(Node current, int data) {
            if (current == null)
                return null;

            if (data == current.data) {
                // No children
                if (current.left == null && current.right == null)
                    return null;

                // One child
                if (current.left == null)
                    return current.right;
                if (current.right == null)
                    return current.left;

                // Two children
                int smallestvalue = findMinValue(current.right);
                current.data = smallestvalue;
                current.right = deleteRec(current.right, smallestvalue);
                return current;
            }

            if (data < current.data) {
                current.left = deleteRec(current.left, data);
            } else {
                current.right = deleteRec(current.right, data);
            }

            return current;
        }

        private int findMinValue(Node node) {
            while (node.left != null) {
                node = node.left;
            }
            return node.data;
        }

        // INORDER
        public void inorderTraversal() {
            System.out.println("Inorder traversal:");
            inorderRec(root);
            System.out.println();
        }

        private void inorderRec(Node node) {
            if (node != null) {
                inorderRec(node.left);
                System.out.print(node.data + " ");
                inorderRec(node.right);
            }
        }

        // PREORDER
        public void preorderTraversal() {
            System.out.println("Preorder traversal:");
            preorderRec(root);
            System.out.println();
        }

        private void preorderRec(Node node) {
            if (node != null) {
                System.out.print(node.data + " ");
                preorderRec(node.left);
                preorderRec(node.right);
            }
        }

        // MIN VALUE
        public int Min() {
            if (root == null) {
                throw new IllegalStateException("Tree is empty");
            }
            return getMinRec(root);
        }

        private int getMinRec(Node node) {
            while (node.left != null) {
                node = node.left;
            }
            return node.data;
        }

        // MAX VALUE
        public int Max() {
            if (root == null) {
                throw new IllegalStateException("Tree is empty");
            }
            return getMaxRec(root);
        }

        private int getMaxRec(Node node) {
            while (node.right != null) {
                node = node.right;
            }
            return node.data;
        }
    }

    public class Main {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            BinarySearchTree.BST bst = new BinarySearchTree.BST();

            while (true) {
                System.out.println("\n======= BINARY SEARCH TREE MENU =======");
                System.out.println("1. Insert");
                System.out.println("2. Delete");
                System.out.println("3. Search");
                System.out.println("4. Inorder Traversal");
                System.out.println("5. Preorder Traversal");
                System.out.println("6. Minimum Value");
                System.out.println("7. Maximum Value");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println("Invalid input. Try again.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        System.out.print("Enter value to insert: ");
                        int insertVal = Integer.parseInt(scanner.nextLine());
                        bst.insert(insertVal);
                        System.out.println(insertVal + " inserted.");
                        break;

                    case 2:
                        System.out.print("Enter value to delete: ");
                        int deleteVal = Integer.parseInt(scanner.nextLine());
                        bst.delete(deleteVal);
                        System.out.println(deleteVal + " deleted if it existed.");
                        break;

                    case 3:
                        System.out.print("Enter value to search: ");
                        int searchVal = Integer.parseInt(scanner.nextLine());
                        boolean found = bst.contains(searchVal);
                        System.out.println(searchVal + (found ? " is present." : " is NOT present."));
                        break;

                    case 4:
                        bst.inorderTraversal();
                        break;

                    case 5:
                        bst.preorderTraversal();
                        break;

                    case 6:
                        try {
                            System.out.println("Minimum value: " + bst.Min());
                        } catch (IllegalStateException e) {
                            System.out.println("Tree is empty.");
                        }
                        break;

                    case 7:
                        try {
                            System.out.println("Maximum value: " + bst.Max());
                        } catch (IllegalStateException e) {
                            System.out.println("Tree is empty.");
                        }
                        break;

                    case 0:
                        System.out.println("Exiting... May your trees be forever balanced!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        }
    }
}
