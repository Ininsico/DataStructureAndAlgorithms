public class CircularLinkedList {
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    static class CircularLinkedListImplementation {
        Node head = null;
        Node tail = null;

        public void addAtBeginning(int data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
                tail = head;
                tail.next = head;
            } else {
                newNode.next = head;
                tail.next = newNode;
                head = newNode;
            }
        }

        public void addAtEnd(int data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
                tail = head;
                tail.next = head;
            } else {
                tail.next = newNode;
                newNode.next = head;
                tail = newNode;
            }
        }

        public void addAfterNode(int data, int key) {
            if (head == null) {
                System.out.println("List is empty. Cannot add after.");
                return;
            }

            Node current = head;
            do {
                if (current.data == key) {
                    Node newNode = new Node(data);
                    newNode.next = current.next;
                    current.next = newNode;
                    if (current == tail) {
                        tail = newNode;
                    }
                    return;
                }
                current = current.next;
            } while (current != head);

            System.out.println("Node with value " + key + " not found.");
        }

        public void addBeforeNode(int data, int key) {
            if (head == null) {
                System.out.println("List is empty. Cannot add before.");
                return;
            }

            Node prev = tail;
            Node current = head;
            do {
                if (current.data == key) {
                    Node newNode = new Node(data);
                    newNode.next = current;
                    prev.next = newNode;
                    if (current == head) {
                        head = newNode;
                    }
                    return;
                }
                prev = current;
                current = current.next;
            } while (current != head);

            System.out.println("Node with value " + key + " not found.");
        }

        public void deleteNode(int key) {
            if (head == null) {
                System.out.println("List is empty. Nothing to delete.");
                return;
            }

            Node prev = tail;
            Node current = head;
            boolean found = false;

            do {
                if (current.data == key) {
                    if (current == head && current == tail) {
                        head = tail = null;
                    } else {
                        prev.next = current.next;
                        if (current == head) {
                            head = current.next;
                        }
                        if (current == tail) {
                            tail = prev;
                        }
                    }
                    found = true;
                    break;
                }
                prev = current;
                current = current.next;
            } while (current != head);

            if (!found) {
                System.out.println("Node with value " + key + " not found.");
            }
        }

        public void display() {
            if (head == null) {
                System.out.println("List is empty.");
                return;
            }

            Node current = head;
            System.out.print("Circular Linked List: ");
            do {
                System.out.print(current.data);
                if (current.next != head) {
                    System.out.print(" -> ");
                }
                current = current.next;
            } while (current != head);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        CircularLinkedListImplementation cll = new CircularLinkedListImplementation();
        java.util.Scanner sc = new java.util.Scanner(System.in);
        int choice;
        
        do {
            System.out.println("\n--- Circular Linked List Operations ---");
            System.out.println("1. Add at beginning");
            System.out.println("2. Add at end");
            System.out.println("3. Add after a node");
            System.out.println("4. Add before a node");
            System.out.println("5. Delete a node");
            System.out.println("6. Display list");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            
            choice = sc.nextInt();
            
            switch (choice) {
                case 1:
                    System.out.print("Enter data to add at beginning: ");
                    cll.addAtBeginning(sc.nextInt());
                    break;
                case 2:
                    System.out.print("Enter data to add at end: ");
                    cll.addAtEnd(sc.nextInt());
                    break;
                case 3:
                    System.out.print("Enter data to add: ");
                    int dataAfter = sc.nextInt();
                    System.out.print("Enter key value after which to add: ");
                    cll.addAfterNode(dataAfter, sc.nextInt());
                    break;
                case 4:
                    System.out.print("Enter data to add: ");
                    int dataBefore = sc.nextInt();
                    System.out.print("Enter key value before which to add: ");
                    cll.addBeforeNode(dataBefore, sc.nextInt());
                    break;
                case 5:
                    System.out.print("Enter key value to delete: ");
                    cll.deleteNode(sc.nextInt());
                    break;
                case 6:
                    cll.display();
                    break;
                case 7:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 7);
        
        sc.close();
    }
}