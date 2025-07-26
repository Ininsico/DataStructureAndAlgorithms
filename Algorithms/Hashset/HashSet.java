package Hashset;

import java.util.Scanner;

public class HashSet {
    private static final int Intialcapacity = 16;
    private static final float Loadfactor = 0.75f;
    private Node[] buckets;
    private int size;

    private static class Node {
        Object key;
        Node next;

        Node(Object key, Node next) {
            this.key = key;
            this.next = next;
        }
    }

    public HashSet() {
        buckets = new Node[Intialcapacity];
        size = 0;
    }

    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }

    public boolean add(Object key) {
        int index = hash(key);
        Node current = buckets[index];
        while (current != null) {
            if (key == null ? current.key == null : key.equals(current)) {
                return false;
            }
            current = current.next;
        }
        buckets[index] = new Node(key, buckets[index]);
        size++;
        if ((float) size / buckets.length > Loadfactor) {
            resize();
        }
        return true;
    }

    public boolean remove(Object key) {
        int index = hash(key);
        Node current = buckets[index];
        Node prev = null;
        while (current != null) {
            if (key == null ? current.key == null : key.equals(current.key)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public boolean contains(Object key) {
        int index = hash(key);
        Node current = buckets[index];
        while (current != null) {
            if (key == null ? current.key == null : key.equals(current.key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public void clear() {
        buckets = new Node[Intialcapacity];
        size = 0;
    }

    private void resize() {
        Node[] oldbuckets = buckets;
        buckets = new Node[oldbuckets.length * 2];
        size = 0;
        for (Node node : oldbuckets) {
            while (node != null) {
                add(node.key);
                node = node.next;
            }
        }
    }

    public void display() {
        System.out.println("[");
        boolean first = true;
        for (Node node : buckets) {
            while (node != null) {
                if (!first) {
                    System.out.println(",");
                }
                System.out.println(node.key);

                first = false;
                node = node.next;
            }
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashSet set = new HashSet();
        int choice;

        do {
            System.out.println("\n--- Custom HashSet Menu ---");
            System.out.println("1. Add an element");
            System.out.println("2. Remove an element");
            System.out.println("3. Check if element exists");
            System.out.println("4. Display all elements");
            System.out.println("5. Get size of set");
            System.out.println("6. Clear the set");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter element to add: ");
                    String addElement = scanner.nextLine();
                    if (set.add(addElement)) {
                        System.out.println("Element added successfully!");
                    } else {
                        System.out.println("Element already exists in the set!");
                    }
                    break;

                case 2:
                    System.out.print("Enter element to remove: ");
                    String removeElement = scanner.nextLine();
                    if (set.remove(removeElement)) {
                        System.out.println("Element removed successfully!");
                    } else {
                        System.out.println("Element not found in the set!");
                    }
                    break;

                case 3:
                    System.out.print("Enter element to check: ");
                    String checkElement = scanner.nextLine();
                    if (set.contains(checkElement)) {
                        System.out.println("Element exists in the set!");
                    } else {
                        System.out.println("Element does not exist in the set!");
                    }
                    break;

                case 4:
                    System.out.print("Elements in set: ");
                    set.display();
                    break;

                case 5:
                    System.out.println("Size of set: " + set.size());
                    break;

                case 6:
                    set.clear();
                    System.out.println("Set cleared successfully!");
                    break;

                case 7:
                    System.out.println("Exiting... Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 7);

        scanner.close();
    }
}
