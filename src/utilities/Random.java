package utilities;

public class Random {
    public static float randFloat(float min, float max) {
        java.util.Random rand = new java.util.Random();
        return rand.nextFloat() * (max - min) + min;
    }

    private Random() {
        // restricted
    }
}