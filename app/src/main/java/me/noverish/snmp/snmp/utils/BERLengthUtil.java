package me.noverish.snmp.snmp.utils;

public class BERLengthUtil {
    public static int getLengthOfInteger(int integer) {
        int mask;
        int intsize = 4;

        /*
         * Truncate "unnecessary" bytes off of the most significant end of this
         * 2's complement integer.  There should be no sequence of 9
         * consecutive 1's or 0's at the most significant end of the
         * integer.
         */
        mask = 0x1FF << ((8 * 3) - 1);
        /* mask is 0xFF800000 on a big-endian machine */
        while((((integer & mask) == 0) || ((integer & mask) == mask))
                && intsize > 1){
            intsize--;
            integer <<= 8;
        }
        return intsize;
    }

    public static int getLengthOfUnsignedInteger(long value) {
        int LENMASK = 0x0ff;

        // figure out the len
        int len = 1;
        if ((( value >> 24) & LENMASK) != 0) {
            len = 4;
        }
        else if ((( value >> 16) & LENMASK) !=0) {
            len = 3;
        }
        else if ((( value >> 8) & LENMASK) !=0) {
            len = 2;
        }
        // check for 5 byte len where first byte will be
        // a null
        if ((( value >> (8 * (len -1))) & 0x080) !=0)	{
            len++;
        }

        return len;
    }
}
