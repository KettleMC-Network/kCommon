package net.kettlemc.kcommon.java;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * A utility class for number operations.
 */
public class NumberUtil {

    private NumberUtil() {
    }

    /**
     * Returns a random Integer between the min and the max range
     *
     * @param min    The lower bound (inclusive)
     * @param max    The upper bound (inclusive)
     * @param random The Random Object to use
     * @return Returns a random Integer between the min and the max range
     */
    public static int randInRange(int min, int max, @NotNull Random random) {
        return random.nextInt((max - min) + 1) + min;
    }

    /**
     * Returns a random Integer between the min and the max range
     *
     * @param min The lower bound (inclusive)
     * @param max The upper bound (inclusive)
     * @return Returns a random Integer between the min and the max range
     */
    public static int randInRange(int min, int max) {
        return randInRange(min, max, new Random());
    }

    /**
     * Checks if the given String is an Integer
     *
     * @param s The String to check
     * @return Whether the given String is an Integer
     */
    public static boolean isInteger(@NotNull String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given String is a Double
     *
     * @param s The String to check
     * @return Whether the given String is a Double
     */
    public static boolean isDouble(@NotNull String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given String is a Float
     *
     * @param s The String to check
     * @return Whether the given String is a Float
     */
    public static boolean isFloat(@NotNull String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given String is a Long
     *
     * @param s The String to check
     * @return Whether the given String is a Long
     */
    public static boolean isLong(@NotNull String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given String is a Short
     *
     * @param s The String to check
     * @return Whether the given String is a Short
     */
    public static boolean isShort(@NotNull String s) {
        try {
            Short.parseShort(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given String is a Byte
     *
     * @param s The String to check
     * @return Whether the given String is a Byte
     */
    public static boolean isByte(@NotNull String s) {
        try {
            Byte.parseByte(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given String is a Number (Integer, Double, Float, Long, Short, Byte)
     *
     * @param s The String to check
     * @return Whether the given String is a Number
     */
    public static boolean isNumber(@NotNull String s) {
        return isInteger(s) || isDouble(s) || isFloat(s) || isLong(s) || isShort(s) || isByte(s);
    }

}
