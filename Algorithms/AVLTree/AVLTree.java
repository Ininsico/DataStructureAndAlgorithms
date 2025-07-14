package AVLTree;

import java.util.Scanner;

public class AVLTree {
    static class Node {
        int key, height;
        Node left;
        Node right;

        public Node(int key) {
            this.key = key;
            this.height = 1;
        }
    }

    private Node root;

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node RightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    private Node LeftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    public void insert(int key) {
        root = insert(root, key);
    }

    private Node insert(Node node, int key) {
        if (node == null)
            return new Node(key);
        if (key < node.key) {
            node.left = insert(node.left, key);
        } else if (key > node.key) {
            node.right = insert(node.right, key);
        } else {
            return node;
        }
        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);
        if (balance > 1 && key < node.left.key) {
            return RightRotate(node);
        }
        if (balance < -1 && key > node.right.key) {
            return LeftRotate(node);
        }
        if (balance > 1 && key > node.left.key) {
            node.left = LeftRotate(node.left);
            return RightRotate(node);
        }
        if (balance < -1 && key < node.right.key) {
            node.right = RightRotate(node);
            return LeftRotate(node);
        }
        return node;
    }

    public void delete(int key) {
        root = delete(root, key);
    }

    private Node delete(Node node, int key) {
        if (root == null)
            return null;
        if (key < root.key) {
            root.left = delete(root.left, key);
        } else if (key > root.key) {
            root.right = delete(root.right, key);
        } else {
            if (root.left == null || root.right == null) {
                Node temp = (root.left != null) ? root.left : root.right;
                if (temp == null)
                    return null;
                root = temp;
            } else {
                Node temp = getminvaluenode(root.right);
                root.key = temp.key;
                root.right = delete(root.right, temp.key);
            }
        }
        root.height = Math.max(height(root.left), height(root.right)) + 1;
        int balance = getBalance(root);
        if (balance > 1 && getBalance(root.left) >= 0) {
            return RightRotate(root);
        }
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = LeftRotate(root.left);
            return RightRotate(root);
        }
        if (balance < -1 && getBalance(root.right) <= 0) {
            return LeftRotate(root);
        }
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = RightRotate(root.right);
            return LeftRotate(root);
        }
        return root;
    }

    private Node getminvaluenode(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public boolean search(int key) {
        return search(root, key);
    }

    private boolean search(Node node, int key) {
        if (node == null)
            return false;
        if (key == node.key)
            return true;
        if (key < node.key)
            return search(node.left, key);
        return search(node.right, key);
    }

    public void inorder() {
        System.out.println("Inorder AVL Tree Transversal:");
        inorder(root);
        System.out.println();
    }

    private void inorder(Node node) {
        while (node != null) {
            inorder(node.left);
            System.out.println(node.key + "");
            inorder(node.right);
        }
    }

    public void preorder() {
        System.out.println("Preorder AVL tree Transversal:");
        preorder(root);
        System.out.println();
    }

    private void preorder(Node node) {
        while (node != null) {
            System.out.println(node.key + "");
            preorder(node.left);
            preorder(node.right);
        }
    }

    public void postorder() {
        System.out.println("Post ordertransversal");
        postorder(root);
        System.out.println();
    }

    private void postorder(Node node) {
        if (node != null) {
            postorder(node.left);
            postorder(node.right);
            System.out.println(node.key + "");
        }
    }

    public int getMin() {
        if (root == null)
            throw new IllegalStateException("Tree is empty.");
        return getminvaluenode(root).key;
    }

    public int getMax() {
        Node current = root;
        if (root == null)
            throw new IllegalStateException("Tree is empty.");
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }

    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n======= AVL TREE MENU =======");
            System.out.println("1. Insert");
            System.out.println("2. Delete");
            System.out.println("3. Search");
            System.out.println("4. Inorder Traversal (Sorted)");
            System.out.println("5. Preorder Traversal");
            System.out.println("6. Postorder Traversal");
            System.out.println("7. Get Minimum");
            System.out.println("8. Get Maximum");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter key to insert: ");
                    int insertKey = sc.nextInt();
                    tree.insert(insertKey);
                    System.out.println("Inserted " + insertKey);
                    break;
                case 2:
                    System.out.print("Enter key to delete: ");
                    int deleteKey = sc.nextInt();
                    tree.delete(deleteKey);
                    System.out.println("Deleted " + deleteKey + " (if existed)");
                    break;
                case 3:
                    System.out.print("Enter key to search: ");
                    int searchKey = sc.nextInt();
                    System.out.println(tree.search(searchKey) ? "Found!" : "Not found.");
                    break;
                case 4:
                    tree.inorder();
                    break;
                case 5:
                    tree.preorder();
                    break;
                case 6:
                    tree.postorder();
                    break;
                case 7:
                    try {
                        System.out.println("Minimum value: " + tree.getMin());
                    } catch (Exception e) {
                        System.out.println("Tree is empty.");
                    }
                    break;
                case 8:
                    try {
                        System.out.println("Maximum value: " + tree.getMax());
                    } catch (Exception e) {
                        System.out.println("Tree is empty.");
                    }
                    break;
                case 0:
                    System.out.println("Exiting... Stay balanced, like your AVL tree.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);
        sc.close();
    }
}
