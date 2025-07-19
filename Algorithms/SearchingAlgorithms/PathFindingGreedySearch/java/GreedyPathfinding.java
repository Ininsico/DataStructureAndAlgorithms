import java.util.*;

public class GreedyPathfinding {
    
    // City coordinates (latitude, longitude approximations)
    static Map<String, double[]> cities = Map.of(
        "Islamabad", new double[]{33.6844, 73.0479},
        "Rawalpindi", new double[]{33.5651, 73.0169},
        "Lahore", new double[]{31.5204, 74.3587},
        "Multan", new double[]{30.1575, 71.5249},
        "Faisalabad", new double[]{31.4180, 73.0790},
        "Sukkur", new double[]{27.7132, 68.8482},
        "Hyderabad", new double[]{25.3969, 68.3778},
        "Karachi", new double[]{24.8607, 67.0011}
    );
    
    // Road connections between cities
    static Map<String, List<String>> roads = Map.of(
        "Islamabad", List.of("Rawalpindi", "Lahore"),
        "Rawalpindi", List.of("Islamabad", "Lahore", "Faisalabad"),
        "Lahore", List.of("Islamabad", "Rawalpindi", "Faisalabad", "Multan"),
        "Faisalabad", List.of("Rawalpindi", "Lahore", "Multan"),
        "Multan", List.of("Lahore", "Faisalabad", "Sukkur"),
        "Sukkur", List.of("Multan", "Hyderabad"),
        "Hyderabad", List.of("Sukkur", "Karachi"),
        "Karachi", List.of("Hyderabad")
    );
    
    static class PathNode implements Comparable<PathNode> {
        String city;
        List<String> path;
        double priority;
        
        PathNode(String city, List<String> path, double priority) {
            this.city = city;
            this.path = new ArrayList<>(path);
            this.priority = priority;
        }
        
        @Override
        public int compareTo(PathNode other) {
            return Double.compare(this.priority, other.priority);
        }
    }
    
    public static double heuristic(String city1, String city2) {
        double[] coord1 = cities.get(city1);
        double[] coord2 = cities.get(city2);
        double dx = coord2[0] - coord1[0];
        double dy = coord2[1] - coord1[1];
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    public static List<String> greedyPath(String start, String goal) {
        Set<String> visited = new HashSet<>();
        PriorityQueue<PathNode> queue = new PriorityQueue<>();
        
        queue.add(new PathNode(start, List.of(start), heuristic(start, goal)));
        
        while (!queue.isEmpty()) {
            PathNode current = queue.poll();
            
            if (current.city.equals(goal)) {
                return current.path;
            }
            
            if (!visited.contains(current.city)) {
                visited.add(current.city);
                
                for (String neighbor : roads.get(current.city)) {
                    if (!visited.contains(neighbor)) {
                        List<String> newPath = new ArrayList<>(current.path);
                        newPath.add(neighbor);
                        double priority = heuristic(neighbor, goal);
                        queue.add(new PathNode(neighbor, newPath, priority));
                    }
                }
            }
        }
        
        return null; // No path found
    }
    
    public static void main(String[] args) {
        List<String> path = greedyPath("Islamabad", "Karachi");
        System.out.println("Greedy Path: " + path);
    }
}