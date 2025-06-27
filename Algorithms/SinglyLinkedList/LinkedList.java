package SinglyLinkedList;

public class LinkedList {
    class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    class List {
        private Node head;

        public List() {
            this.head = null;
        }

        public void insertAtEnd(int data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
            } else {
                Node current = head;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
            }
        }

        public void insertAtStart(int data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
            } else {
                newNode.next = head;
                head = newNode;
            }
        }

        public void insertAfterNode(int data, int key) {
            Node newNode = new Node(data);
            if (head == null) {
                System.out.println("The list is empty");
                return;
            }
            Node current = head;
            while (current != null) {
                if (current.data == key) {
                    newNode.next = current.next;
                    current.next = newNode;
                    return;
                }
                current = current.next;
            }
            System.out.println("The Node you want to add the element next to is not found");
        }

        public void insertBeforeNode(int data, int key) {
            Node newNode = new Node(data);
            if (head == null) {
                System.out.println("The List is empty");
                return;
            }
            if (head.data == key) {
                newNode.next = head;
                head = newNode;
                return;
            }
            Node current = head.next;
            Node prev = head;
            while (current != null) {
                if (current.data == key) {
                    prev.next = newNode;
                    newNode.next = current;
                    return;
                }
                prev = current;
                current = current.next;
            }
            System.out.println("The element you are trying to insert before is not found");
        }

        public void deleteNode(int data) {
            if (head == null) {
                System.out.println("The list is empty");
                return;
            }
            if (head.data == data) {
                head = head.next;
                System.out.println("Node deleted");
                return;
            }
            Node current = head.next;
            Node prev = head;
            while (current != null) {
                if (current.data == data) {
                    prev.next = current.next;
                    System.out.println("Node successfully deleted");
                    return;
                }
                prev = current;
                current = current.next;
            }
            System.out.println("No element found with the given data");
        }

        public void display() {
            if (head == null) {
                System.out.println("The list is empty");
            } else {
                Node current = head;
                while (current != null) {
                    System.out.print(current.data + "-->");
                    current = current.next;
                }
                System.out.println("Null");
            }
        }
    }

    public static void main(String[] args) {
        LinkedList linkedList = new LinkedList();
        List list = linkedList.new List();
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int choice;
        do {
            System.out.println("The SinglyLinked list MENU");
            System.out.println("1: Insert At end");
            System.out.println("2: Insert at front");
            System.out.println("3: Delete a Node");
            System.out.println("4: Displaying the linkedlist");
            System.out.println("5: Insert Before a node");
            System.out.println("6: Insert after a node");
            System.out.println("0: Exit");
            System.out.println("Enter a choice");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter the data you want to add");
                    int dataToAddAtEnd = scanner.nextInt();
                    list.insertAtEnd(dataToAddAtEnd);
                    break;
                case 2:
                    System.out.println("Enter the data you want to add ");
                    int data2 = scanner.nextInt();
                    list.insertAtStart(data2);
                    break;
                case 3:
                    System.out.println("Enter the data you want to delete");
                    int data3 = scanner.nextInt();
                    list.deleteNode(data3);
                    break;
                case 4:
                    System.out.println("Displaying the Linked List");
                    list.display();
                    break;
                case 5:
                    System.out.println("Enter the node you want to add before");
                    int data4 = scanner.nextInt();
                    System.out.println("Enter the data you want to add before the node");
                    int data5 = scanner.nextInt();
                    list.insertBeforeNode(data5, data4);
                    break;
                case 6:
                    System.out.println("Enter the node you want to insert after");
                    int data6 = scanner.nextInt();
                    System.out.println("Enter the data you want to add after the node");
                    int data7 = scanner.nextInt();
                    list.insertAfterNode(data7, data6);
                    break;
                case 0:
                    System.out.println("Exiting");
                    break;
                default:
                    System.out.println("Invalid choice enter the number from the list");
            }
        } while (choice != 0);
        scanner.close();
    }
}