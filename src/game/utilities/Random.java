package game.utilities;

/**
 * A few utility functions for generating random values
 */
public class Random {
    /**
     * Generate a random number in range
     * @param min: low bounds for random number
     * @param max: high bounds for random number
     * @return: random number
     */
    public static float randFloat(float min, float max) {
        java.util.Random rand = new java.util.Random();
        return rand.nextFloat() * (max - min) + min;
    }

    /**
     * Generate a random string of a given length (i.e for file naming)
     * @param length: desired length of string
     * @return: random string
     */
    public static String randString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        while (salt.length() < length) {
            int index = (int) (Math.random() * chars.length());
            salt.append(chars.charAt(index));
        }
        return salt.toString();

    }

    /**
     * Init random functions
     */
    private Random() {
        // restricted
    }
}