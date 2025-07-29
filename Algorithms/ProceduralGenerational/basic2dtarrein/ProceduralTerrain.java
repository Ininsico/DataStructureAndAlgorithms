package ProceduralGenerational.basic2dtarrein;

import java.util.Random;

public class ProceduralTerrain {

    // Dimensions of the terrain grid
    static final int WIDTH = 20;
    static final int HEIGHT = 10;

    public static void main(String[] args) {
        int[][] terrain = generateTerrain(WIDTH, HEIGHT);
        printTerrain(terrain);
    }

    // Generate terrain with random height values
    public static int[][] generateTerrain(int width, int height) {
        int[][] grid = new int[height][width];
        Random rand = new Random();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Height value between 0 and 9
                grid[y][x] = rand.nextInt(10);
            }
        }
        return grid;
    }

    // Print terrain to console
    public static void printTerrain(int[][] terrain) {
        for (int y = 0; y < terrain.length; y++) {
            for (int x = 0; x < terrain[y].length; x++) {
                System.out.print(terrain[y][x] + " ");
            }
            System.out.println();
        }
    }
}
