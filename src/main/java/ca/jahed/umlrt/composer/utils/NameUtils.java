package ca.jahed.umlrt.composer.utils;

import java.util.Random;

public class NameUtils {
    static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final Random random = new Random(0);

    public static String randomString(String prefix, int length) {
        return prefix + randomString(length);
    }

    public static String randomString(int length) {
        StringBuilder str = new StringBuilder();
        while (str.length() < length)
            str.append(chars.charAt((int) (random.nextFloat() * chars.length())));
        return str.toString();
    }
}
