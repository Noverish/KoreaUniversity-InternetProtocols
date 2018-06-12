package me.noverish.snmp.utils;

public class Utils {
    public static int getHexLengthOfInteger(int number) {
        String tmp = Integer.toHexString(number);
        return (int) Math.ceil((double) tmp.length() / 2);
    }

    public static int getHexLengthOfLong(long number) {
        String tmp = Long.toHexString(number);
        return (int) Math.ceil((double) tmp.length() / 2);
    }
}
