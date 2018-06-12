package me.noverish.snmp.utils;

public class ArrayUtil {
    public static byte[] concat(byte[]... arrs) {
        int totalLength = 0;
        for (byte[] arr : arrs) totalLength += arr.length;

        byte[] output = new byte[totalLength];

        int now = 0;
        for (byte[] arr : arrs) {
            System.arraycopy(arr, 0, output, now, arr.length);
            now += arr.length;
        }

        return output;
    }

    public static void print(byte[] arr) {
        for (byte b : arr) {
            System.out.printf("%02X ", b);
        }
        System.out.print("\n");
    }
}
