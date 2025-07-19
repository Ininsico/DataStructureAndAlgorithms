package DFS.java;

import java.util.*;

public class DFS {
    
    public static class DFSResult {
        List<String> visitedOrder;
        Map<String, String> parentMap;
        
        public DFSResult(List<String> visitedOrder, Map<String, String> parentMap) {
            this.visitedOrder = visitedOrder;
            this.parentMap = parentMap;
        }
    }
    
    // Iterative DFS
    public static DFSResult dfs(Map<String, List<String>> graph, String start) {
        List<String> visitedOrder = new ArrayList<>();
        Map<String, String> parentMap = new HashMap<>();
        Stack<String> stack = new Stack<>();
        
        stack.push(start);
        parentMap.put(start, null);
        
        while (!stack.isEmpty()) {
            String node = stack.pop();
            
            if (!visitedOrder.contains(node)) {
                visitedOrder.add(node);
                
                // Push neighbors in reverse order to visit them in order
                List<String> neighbors = graph.get(node);
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    String neighbor = neighbors.get(i);
                    if (!parentMap.containsKey(neighbor)) {
                        parentMap.put(neighbor, node);
                        stack.push(neighbor);
                    }
                }
            }
        }
        
        return new DFSResult(visitedOrder, parentMap);
    }
    
    // Recursive DFS
    public static void dfsRecursive(
        Map<String, List<String>> graph, 
        String node, 
        List<String> visitedOrder, 
        Map<String, String> parentMap
    ) {
        visitedOrder.add(node);
        
        for (String neighbor : graph.get(node)) {
            if (!parentMap.containsKey(neighbor)) {
                parentMap.put(neighbor, node);
                dfsRecursive(graph, neighbor, visitedOrder, parentMap);
            }
        }
    }
    
    public static void main(String[] args) {
        Map<String, List<String>> graph = new HashMap<>();
        graph.put("A", Arrays.asList("B", "C"));
        graph.put("B", Arrays.asList("A", "D", "E"));
        graph.put("C", Arrays.asList("A", "F"));
        graph.put("D", Arrays.asList("B"));
        graph.put("E", Arrays.asList("B", "F"));
        graph.put("F", Arrays.asList("C", "E"));
        
        System.out.println("Iterative DFS:");
        DFSResult result = dfs(graph, "A");
        System.out.println("Visited order: " + result.visitedOrder);
        System.out.println("Parent relationships: " + result.parentMap);
        
        System.out.println("\nRecursive DFS:");
        List<String> visitedRec = new ArrayList<>();
        Map<String, String> parentRec = new HashMap<>();
        parentRec.put("A", null);
        dfsRecursive(graph, "A", visitedRec, parentRec);
        System.out.println("Visited order: " + visitedRec);
        System.out.println("Parent relationships: " + parentRec);
    }
}