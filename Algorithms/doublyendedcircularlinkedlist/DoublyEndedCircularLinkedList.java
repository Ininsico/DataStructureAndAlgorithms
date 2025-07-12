package doublyendedcircularlinkedlist;

import java.util.Scanner;

public class DoublyEndedCircularLinkedList {
    static class Node {
        int data;
        Node next;
        Node prev;

        Node(int data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    static class DCLL {
        Node head;
        Node tail;

        public DCLL() {
            this.head = null;
            this.tail = null;
        }

        public void AddAtBeginning(int data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = tail = newNode;
                head.next = head;
                head.prev = head;
            } else {
                newNode.next = head;
                newNode.prev = tail;
                head.prev = newNode;
                tail.next = newNode;
                head = newNode;
            }
        }

        public void AddAtEnd(int data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = tail = newNode;
                head.next = head;
                head.prev = head;
            } else {
                newNode.next = head;
                newNode.prev = tail;
                tail.next = newNode;
                head.prev = newNode;
                tail = newNode;
            }
        }

        public void AddAfterANode(int data, int key) {
            if (head == null) {
                System.out.println("List is empty. Cannot add after.");
                return;
            }
            
            Node current = head;
            do {
                if (current.data == key) {
                    Node newNode = new Node(data);
                    newNode.next = current.next;
                    newNode.prev = current;
                    current.next.prev = newNode;
                    current.next = newNode;
                    
                    if (current == tail) {
                        tail = newNode;
                    }
                    return;
                }
                current = current.next;
            } while (current != head);
            
            System.out.println("Key not found in the list.");
        }

        public void AddBeforeANode(int data, int key) {
            if (head == null) {
                System.out.println("List is empty. Cannot add before.");
                return;
            }
            
            if (head.data == key) {
                AddAtBeginning(data);
                return;
            }
            
            Node current = head.next;
            while (current != head) {
                if (current.data == key) {
                    Node newNode = new Node(data);
                    newNode.prev = current.prev;
                    newNode.next = current;
                    current.prev.next = newNode;
                    current.prev = newNode;
                    return;
                }
                current = current.next;
            }
            
            System.out.println("Key not found in the list.");
        }

        public void display() {
            if (head == null) {
                System.out.println("List is empty.");
                return;
            }
            
            System.out.print("List: ");
            Node current = head;
            do {
                System.out.print(current.data + " ");
                current = current.next;
            } while (current != head);
            System.out.println();
        }

        public void deleteNode(int key) {
            if (head == null) {
                System.out.println("List is empty. Cannot delete.");
                return;
            }
            
            Node current = head;
            do {
                if (current.data == key) {
                    if (current == head && current == tail) {
                        head = tail = null;
                    } else if (current == head) {
                        head = head.next;
                        head.prev = tail;
                        tail.next = head;
                    } else if (current == tail) {
                        tail = tail.prev;
                        tail.next = head;
                        head.prev = tail;
                    } else {
                        current.prev.next = current.next;
                        current.next.prev = current.prev;
                    }
                    return;
                }
                current = current.next;
            } while (current != head);
            
            System.out.println("Key not found in the list.");
        }

        public void displayReverse() {
            if (head == null) {
                System.out.println("List is empty.");
                return;
            }
            
            System.out.print("Reversed List: ");
            Node current = tail;
            do {
                System.out.print(current.data + " ");
                current = current.prev;
            } while (current != tail);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        DCLL dcll = new DCLL();
        Scanner sc = new Scanner(System.in);
        int choice, data, key;
        
        System.out.println("Welcome to Doubly Ended Circular Linked List Implementation");
        
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add at Beginning");
            System.out.println("2. Add at End");
            System.out.println("3. Add After a Node");
            System.out.println("4. Add Before a Node");
            System.out.println("5. Display");
            System.out.println("6. Delete a Node");
            System.out.println("7. Display in Reverse");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            
            switch (choice) {
                case 1:
                    System.out.print("Enter data to add at beginning: ");
                    data = sc.nextInt();
                    dcll.AddAtBeginning(data);
                    break;
                case 2:
                    System.out.print("Enter data to add at end: ");
                    data = sc.nextInt();
                    dcll.AddAtEnd(data);
                    break;
                case 3:
                    System.out.print("Enter data to add: ");
                    data = sc.nextInt();
                    System.out.print("Enter key after which to add: ");
                    key = sc.nextInt();
                    dcll.AddAfterANode(data, key);
                    break;
                case 4:
                    System.out.print("Enter data to add: ");
                    data = sc.nextInt();
                    System.out.print("Enter key before which to add: ");
                    key = sc.nextInt();
                    dcll.AddBeforeANode(data, key);
                    break;
                case 5:
                    dcll.display();
                    break;
                case 6:
                    System.out.print("Enter key to delete: ");
                    key = sc.nextInt();
                    dcll.deleteNode(key);
                    break;
                case 7:
                    dcll.displayReverse();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}