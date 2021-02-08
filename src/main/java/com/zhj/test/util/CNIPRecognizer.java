package com.zhj.test.util;

public class CNIPRecognizer {
    public static boolean isValidIpV4Address(String value) {
        int periods = 0;
        int i;
        int length = value.length();
        if (length > 15) {
            return false;
        }
        char c;
        StringBuilder word = new StringBuilder();
        for (i = 0; i < length; i ++) {
            c = value.charAt(i);
            if (c == '.') {
                periods ++;
                if (periods > 3) {
                    return false;
                }
                if (word.length() == 0) {
                    return false;
                }
                if (Integer.parseInt(word.toString()) > 255) {
                    return false;
                }
                word.delete(0, word.length());
            } else if (!Character.isDigit(c)) {
                return false;
            } else {
                if (word.length() > 2) {
                    return false;
                }
                word.append(c);
            }
        }
        if (word.length() == 0 || Integer.parseInt(word.toString()) > 255) {
            return false;
        }
        return periods == 3;
    }
    public static long ipToLong(String ipAddress) {
        String[] addrArray = ipAddress.split("\\.");
        long num = 0;
        for (int i = 0; i < addrArray.length; i++) {
            int power = 3 - i;
            // 1. (192 % 256) * 256 pow 3
            // 2. (168 % 256) * 256 pow 2
            // 3. (2 % 256) * 256 pow 1
            // 4. (1 % 256) * 256 pow 0
            num += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power)));
        }
        return num;
    }

    public static String longToIp(long i) {
        return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + (i & 0xFF);
    }
}
