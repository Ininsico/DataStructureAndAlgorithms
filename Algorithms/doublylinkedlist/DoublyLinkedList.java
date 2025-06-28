package doublylinkedlist;

import java.util.Scanner;

public class DoublyLinkedList {
    class Node {
        int data;
        Node next;
        Node prev;

        Node(int data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    class DoublyLinkedListImpl {
        Node head;
        Node tail;

        public DoublyLinkedListImpl() {
            this.head = null;
            this.tail = null;
        }

        public void addBeforeNode(int data, int key) {
            Node newNode = new Node(data);
            if (head == null) {
                System.out.println("List is empty. Node not found.");
                return;
            }

            Node current = head;
            while (current != null) {
                if (current.data == key) {
                    newNode.next = current;
                    newNode.prev = current.prev;
                    if (current.prev != null) {
                        current.prev.next = newNode;
                    } else {
                        head = newNode;
                    }
                    current.prev = newNode;
                    return;
                }
                current = current.next;
            }
            System.out.println("Node with key " + key + " not found.");
        }

        public void addAfterNode(int data, int key) {
            Node newNode = new Node(data);
            if (head == null) {
                System.out.println("The list is empty");
                return;
            }

            Node current = head;
            while (current != null) {
                if (current.data == key) {
                    newNode.prev = current;
                    newNode.next = current.next;
                    if (current.next != null) {
                        current.next.prev = newNode;
                    } else {
                        tail = newNode;
                    }
                    current.next = newNode;
                    return;
                }
                current = current.next;
            }
            System.out.println("Node with key " + key + " not found.");
        }

        public void addAtStart(int data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            }
        }

        public void addAtEnd(int data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
        }

        public void display() {
            if (head == null) {
                System.out.println("The list is empty");
                return;
            }

            Node current = head;
            System.out.print("null <- ");
            while (current != null) {
                System.out.print(current.data);
                if (current.next != null) {
                    System.out.print(" <-> ");
                } else {
                    System.out.print(" -> ");
                }
                current = current.next;
            }
            System.out.println("null");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DoublyLinkedList dll = new DoublyLinkedList();
        DoublyLinkedListImpl list = dll.new DoublyLinkedListImpl();

        int choice;
        do {
            System.out.println("\nDoubly Linked List Implementation");
            System.out.println("Choose an operation:");
            System.out.println("1: Add elements at the end");
            System.out.println("2: Add elements at the start");
            System.out.println("3: Add elements after a node");
            System.out.println("4: Add elements before a node");
            System.out.println("5: Display the list");
            System.out.println("0: Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter the data you want to add at the end: ");
                    int endData = scanner.nextInt();
                    list.addAtEnd(endData);
                    break;
                case 2:
                    System.out.print("Enter the data you want to add at the start: ");
                    int startData = scanner.nextInt();
                    list.addAtStart(startData);
                    break;
                case 3:
                    System.out.print("Enter the data you want to add after a node: ");
                    int afterData = scanner.nextInt();
                    System.out.print("Enter the key of the node you want to add after: ");
                    int afterKey = scanner.nextInt();
                    list.addAfterNode(afterData, afterKey);
                    break;
                case 4:
                    System.out.print("Enter the data you want to add before a node: ");
                    int beforeData = scanner.nextInt();
                    System.out.print("Enter the key of the node you want to add before: ");
                    int beforeKey = scanner.nextInt();
                    list.addBeforeNode(beforeData, beforeKey);
                    break;
                case 5:
                    list.display();
                    break;
                case 0:
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }
}