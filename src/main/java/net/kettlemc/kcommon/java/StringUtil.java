package net.kettlemc.kcommon.java;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A utility class for String operations.
 */
public final class StringUtil {

    private StringUtil() {
    }

    /**
     * Combines the given arguments into one String, separated by the given String
     *
     * @param args          The arguments to combine
     * @param insertBetween The String to insert between the arguments
     * @return The combined String
     */
    public static String combineArgs(String[] args, String insertBetween) {
        return combineArgs(Arrays.asList(args), insertBetween);
    }

    /**
     * Combines the given arguments into one String, separated by the given String
     *
     * @param args          The arguments to combine
     * @param insertBetween The String to insert between the arguments
     * @return The combined String
     */
    public static String combineArgs(List<String> args, String insertBetween) {
        StringBuilder combined = new StringBuilder();
        for (String arg : args)
            if (combined.length() == 0)
                combined.append(arg);
            else
                combined.append(insertBetween).append(arg);

        return combined.toString();
    }

    /**
     * Returns the current date as a String with the given pattern
     *
     * @param pattern The pattern to use (e.g. "yyyy-MM-dd_HH-mm-ss")
     * @return The current date as a String with the given pattern
     */
    public static String currentDate(String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date());
    }

    /**
     * Returns the current date as a String with the pattern "yyyy-MM-dd_HH-mm-ss"
     *
     * @return The current date as a String with the pattern "yyyy-MM-dd_HH-mm-ss"
     */
    public static String currentDate() {
        return currentDate("yyyy-MM-dd_HH-mm-ss");
    }

    /**
     * Removes all emojis from the given String
     *
     * @param input The String to remove emojis from
     * @return The String without emojis
     */
    public static String stripEmojis(String input) {
        return input.replaceAll("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]", "");
    }

}