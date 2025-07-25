package LinkedListPracticeQuestions.ReverseALinkedList;

public class ReverseLl {

    public static class Node {
        int val;
        Node next;

        public Node(int val) {
            this.val = val;
            this.next = null;
        }
    }

   
    public static class Solution {
        public Node reverseList(Node head) {
            Node prev = null;
            Node current = head;

            while (current != null) {
                Node next = current.next;
                current.next = prev;
                prev = current;
                current = next;
            }

            return prev;
        }
    }
    public static void main(String[] args) {
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);

        Solution sol = new Solution();
        Node reversedHead = sol.reverseList(head);

        Node temp = reversedHead;
        while (temp != null) {
            System.out.print(temp.val + " ");
            temp = temp.next;
        }
    }
}
