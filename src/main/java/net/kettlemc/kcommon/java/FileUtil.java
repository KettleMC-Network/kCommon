package net.kettlemc.kcommon.java;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A utility class for file operations.
 */
public class FileUtil {

    private FileUtil() {
    }

    /**
     * Saves a resource to a file.
     *
     * @param clazz    The class to get the resource from.
     * @param resource The relative path of the resource to save.
     * @param output   The path to save the resource to.
     * @return Whether the resource was saved successfully.
     */
    public static boolean saveResourceAsFile(@NotNull Class<?> clazz, @NotNull String resource, @NotNull Path output) {
        URL url = clazz.getResource(resource);
        if (url == null) {
            return false;
        }
        return saveResourceAsFile(url, output);
    }

    /**
     * Saves a resource to a file.
     *
     * @param resource The URL of the resource to save.
     * @param output   The path to save the resource to.
     * @return Whether the resource was saved successfully.
     */
    public static boolean saveResourceAsFile(@NotNull URL resource, @NotNull Path output) {
        try {
            InputStream in = resource.openStream();
            output.toFile().getParentFile().mkdirs();
            Files.copy(in, output);
            in.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
