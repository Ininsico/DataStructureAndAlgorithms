package HufmanCoding;

import java.util.*;

public class HuffmanCoding {
    static class HuffmanNode {
        char character;
        int freq;
        HuffmanNode left, right;

        public HuffmanNode(char character, int freq) {
            this.character = character;
            this.freq = freq;
            this.left = this.right = null;
        }
    }

    static class FrequencyComprator implements Comparator<HuffmanNode> {
        public int compare(HuffmanNode a, HuffmanNode b) {
            return a.freq - b.freq;
        }
    }

    private void generatecode(HuffmanNode root, String code, Map<Character, String> HuffmanCodes) {
        if (root == null)
            return;
        if (root.left == null && root.right == null) {
            HuffmanCodes.put(root.character, code);
            return;
        }
        generatecode(root.left, code + "0", HuffmanCodes);
        generatecode(root.right, code + "1", HuffmanCodes);
    }

    public void buildHuffmanTree(String Text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : Text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        
        // Handle case where input string has only one unique character
        if (freqMap.size() == 1) {
            char onlyChar = Text.charAt(0);
            Map<Character, String> HuffmanCodes = new HashMap<>();
            HuffmanCodes.put(onlyChar, "0");
            System.out.println("Huffman Codes:");
            System.out.println(onlyChar + " : 0");
            System.out.println("Encoded Text: " + "0".repeat(Text.length()));
            return;
        }

        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(new FrequencyComprator());
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }
        
        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode merged = new HuffmanNode('-', left.freq + right.freq);
            merged.left = left;
            merged.right = right;
            pq.add(merged);  // This line was missing in your original code
        }
        
        HuffmanNode root = pq.poll();
        Map<Character, String> HuffmanCodes = new HashMap<>();
        generatecode(root, "", HuffmanCodes);
        
        System.out.println("Huffman Codes:");
        for (Map.Entry<Character, String> entry : HuffmanCodes.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        
        StringBuilder encoded = new StringBuilder();
        for (char c : Text.toCharArray()) {
            encoded.append(HuffmanCodes.get(c));
        }
        System.out.println("Encoded Text: " + encoded.toString());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HuffmanCoding huffman = new HuffmanCoding();
        System.out.println("Enter A string to code through");
        String input = sc.nextLine();
        huffman.buildHuffmanTree(input);
        sc.close();
    }
}