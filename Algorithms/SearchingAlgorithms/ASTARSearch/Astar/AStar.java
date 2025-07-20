import java.util.*;

public class AStar {
    static class Node {
        int x, y;
        Node parent;
        int g, h, f;
        
        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static List<Node> aStar(int[][] grid, Node start, Node goal) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(a -> a.f));
        Set<Node> closedSet = new HashSet<>();
        start.g = 0;
        start.h = heuristic(start, goal);
        start.f = start.g + start.h;
        openSet.add(start);

        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // 4-directional

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.x == goal.x && current.y == goal.y) {
                return reconstructPath(current);
            }
            closedSet.add(current);
            for (int[] dir : dirs) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];
                if (nx < 0 || ny < 0 || nx >= grid.length || ny >= grid[0].length || grid[nx][ny] == 1) {
                    continue; // Skip obstacles and out-of-bounds
                }
                Node neighbor = new Node(nx, ny);
                if (closedSet.contains(neighbor)) continue;
                int tentativeG = current.g + 1;
                if (!openSet.contains(neighbor) || tentativeG < neighbor.g) {
                    neighbor.parent = current;
                    neighbor.g = tentativeG;
                    neighbor.h = heuristic(neighbor, goal);
                    neighbor.f = neighbor.g + neighbor.h;
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
        return null; // No path found
    }

    private static int heuristic(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // Manhattan distance
    }

    private static List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

   public static void main(String[] args) {
        int[][] grid = {
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 1},
            {0, 1, 1, 0, 0},
            {0, 0, 0, 0, 0}
        };
        Node start = new Node(0, 0);
        Node goal = new Node(4, 4);
        List<Node> path = aStar(grid, start, goal);
        if (path != null) {
            for (Node node : path) {
                System.out.println("Node: (" + node.x + ", " + node.y + ")");
            }
        } else {
            System.out.println("No path found");
        }
    }
}