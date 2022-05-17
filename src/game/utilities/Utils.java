package game.utilities;

import game.userIO.UiConstants;

public class Utils {
    public static float randFloat(float min, float max) {
        java.util.Random rand = new java.util.Random();
        return rand.nextFloat() * (max - min) + min;
    }

    public static String randString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        while (salt.length() < length) {
            int index = (int) (Math.random() * chars.length());
            salt.append(chars.charAt(index));
        }
        return salt.toString();

    }

    public static int[][] breakIntoChunks(int size) {
        int increment = size / UiConstants.threadCount + 1;
        int[][] positions = new int[UiConstants.threadCount][2];
        for (int i = 0; i<UiConstants.threadCount; i++) {
            int start = i * increment;
            int end = (i + 1) * increment;
            if (start >= size) {
                break;
            }
            if (end > size) {
                end = size;
            }
            positions[i][0] = start;
            positions[i][1] = end;
        }
        return positions;
    }

    public static boolean inBounds(float value, float lowBound, float highBound) {
        return (value >= lowBound && value < highBound);
    }

    private Utils() {
        // restricted
    }
}