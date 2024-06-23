package root.app.util;

import java.util.Random;

public class GenerateStringUtil {

    public static String generateString(int length) {
        Random random = new Random();
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(str.charAt(random.nextInt(str.length())));
        }
        return sb.toString();
    }
}
