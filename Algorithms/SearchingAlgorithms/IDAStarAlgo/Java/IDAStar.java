import java.util.*;

public class IDAStar {
    static class Node {
        int x, y;
        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x && y == node.y;
        }
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private static int heuristic(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private static int search(List<Node> path, int g, int bound, int[][] grid, Node goal) {
        Node node = path.get(path.size() - 1);
        int f = g + heuristic(node, goal);
        if (f > bound) return f;
        if (node.x == goal.x && node.y == goal.y) return -1; // FOUND
        int min = Integer.MAX_VALUE;
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (int[] dir : dirs) {
            Node neighbor = new Node(node.x + dir[0], node.y + dir[1]);
            if (neighbor.x < 0 || neighbor.y < 0 || neighbor.x >= grid.length || neighbor.y >= grid[0].length || grid[neighbor.x][neighbor.y] == 1) {
                continue;
            }
            if (path.contains(neighbor)) continue;
            path.add(neighbor);
            int res = search(path, g + 1, bound, grid, goal);
            if (res == -1) return -1;
            if (res < min) min = res;
            path.remove(path.size() - 1);
        }
        return min;
    }

    public static List<Node> idaStar(int[][] grid, Node start, Node goal) {
        int bound = heuristic(start, goal);
        List<Node> path = new ArrayList<>();
        path.add(start);
        while (true) {
            int res = search(path, 0, bound, grid, goal);
            if (res == -1) return path;
            if (res == Integer.MAX_VALUE) return null; // No path
            bound = res;
        }
    }

    public static void main(String[] args) {
        int[][] grid = {
            {0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0},
            {0, 0, 1, 0, 0},
            {0, 1, 0, 1, 0},
            {0, 0, 0, 0, 0}
        };
        Node start = new Node(0, 0);
        Node goal = new Node(4, 4);
        List<Node> path = idaStar(grid, start, goal);
        System.out.println("IDA* Path:");
        if (path != null) {
            for (Node node : path) {
                System.out.println("(" + node.x + ", " + node.y + ")");
            }
        } else {
            System.out.println("No path found!");
        }
    }
}