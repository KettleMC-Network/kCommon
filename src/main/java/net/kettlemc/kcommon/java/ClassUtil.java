package net.kettlemc.kcommon.java;

public class ClassUtil {

    private ClassUtil() {
    }

    /**
     * @param clazz Class you want to check
     * @return Whether the provided class is loaded
     */
    public static boolean isClassPresent(String clazz) {
        try {
            Class.forName(clazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
