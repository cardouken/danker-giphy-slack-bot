package app.albums.giphy.util;

import java.util.Random;

public class StringUtility {

    private static final char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] chardigits = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] digits = "0123456789".toCharArray();

    private static Random rnd = new Random();

    public static String createRandomCharString(int size) {
        return createRandomString(chars, size);
    }

    public static String createRandomString(int size) {
        return createRandomString(chardigits, size);
    }

    private static String createRandomString(char[] data, int size) {
        if (size <= 0) {
            return null;
        }
        StringBuilder result = new StringBuilder(size);

        for (int i = 0; i < size; ++i) {
            char c = data[rnd.nextInt(data.length)];
            result.append(c);
        }
        return result.toString();
    }

    public static String createRandomDigitString(int size) {
        return createRandomString(digits, size);
    }
}