package CirculatLinkedList;

public class CircularLinkedList {
    class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    class List {
        Node head;
        Node tail;

        List() {
            Node head = null;
            Node tail = null;
        }

        public void insertatbeganning(int data) {
            Node newnode = new Node(data);
            if (head == null) {
                head = newnode;
                tail = newnode;
                tail.next = head;
            } else {
                newnode.next = head;
                head = newnode;
                tail.next = head;
            }
        }

        public void insertatend(int data) {
            Node newnode = new Node(data);
            if (head == null) {
                head = newnode;
                tail = newnode;
                tail.next = head;
            } else {
                newnode.next = tail.next;
                tail.next = newnode;
                tail = newnode;
            }
        }

        public void addafteranode(int data, int key) {
            Node newnode = new Node(data);
            if (head == null) {
                System.out.println("The list is empty");
            } else {
                Node current = head;
                do {
                    if (current.data == key) {
                        newnode.next = current.next;
                        current.next = newnode;
                        if (current == tail) {
                            tail = newnode;
                        }
                    }
                } while (current.next != head && current.data != key);
                if (current.data != key) {
                    System.out.println("The Node you want to add the element next to is not found");
                }
            }
        }

        public void addbeforenode(int data, int key) {
            Node newnode = new Node(data);
            if (head == null) {
                System.out.println("The list is empty");
            } else if (head.data == key) {
                newnode.next = head;
                Node current = head;
                while (current.next != head) {

                }
            }
        }
    }
}
