import java.util.Random;

public class HillClimbing {
    private static final Random random = new Random();

    public static void main(String[] args) {
        double[] result = hillClimbing(1000, 0.1);
        System.out.printf("Best x: %.2f, Best value: %.2f%n", result[0], result[1]);
    }

    public static double[] hillClimbing(int maxIter, double stepSize) {
        // Start at a random x
        double currentX = random.nextDouble() * 20 - 10; // Range: [-10, 10]
        double currentVal = -Math.pow(currentX, 2) + 4 * currentX;

        for (int i = 0; i < maxIter; i++) {
            // Generate a neighboring solution
            double neighborX = currentX + (random.nextDouble() * 2 - 1) * stepSize;
            double neighborVal = -Math.pow(neighborX, 2) + 4 * neighborX;

            // Move to the neighbor if it's better
            if (neighborVal > currentVal) {
                currentX = neighborX;
                currentVal = neighborVal;
            }
        }

        return new double[]{currentX, currentVal};
    }
}