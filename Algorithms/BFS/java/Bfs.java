package BFS.java;

import java.util.*;

public class Bfs {
    public static class BFSresult {
        List<String> visitedorder;
        Map<String, String> parentMap;

        public BFSresult(List<String> visitedorder, Map<String, String> parentMap) {
            this.visitedorder = visitedorder;
            this.parentMap = parentMap;
        }
    }

    public static BFSresult bfs(Map<String, List<String>> graph, String start) {
        List<String> visitedorder = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parentMap = new HashMap<>();
        queue.add(start);
        parentMap.put(start, null);
        while (!queue.isEmpty()) {
            String node = queue.poll();
            if (!visitedorder.contains(node)) {
                visitedorder.add(node);
                for (String Nrighbour : graph.get(node)) {
                    if (!parentMap.containsKey(Nrighbour)) {
                        parentMap.put(Nrighbour, node);
                        queue.add(Nrighbour);

                    }
                }
            }
        }
        return new BFSresult(visitedorder, parentMap);
    }

    public static List<String> getPath(Map<String, String> parentMap, String node) {
        List<String> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = parentMap.get(node);
        }
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = new HashMap<>();
        graph.put("A", Arrays.asList("B", "C"));
        graph.put("B", Arrays.asList("A", "D", "E"));
        graph.put("C", Arrays.asList("A", "F"));
        graph.put("D", Arrays.asList("B"));
        graph.put("E", Arrays.asList("B", "F"));
        graph.put("F", Arrays.asList("C", "E"));
        String startNode = "A";
        BFSresult result = bfs(graph, startNode);
        System.out.println("Visited Order: " + result.visitedorder);
        System.out.println("Parent Relationships: " + result.parentMap);
        System.out.println("Path from A to E: " + getPath(result.parentMap, "E"));
    }
}
