package com.tschanz.v_bro.common.xml;


public class CharByteCounter {
    private static final int CHAR_ONE_BYTE_MASK = 0xFFFFFF80;
    private static final int CHAR_TWO_BYTES_MASK = 0xFFFFF800;
    private static final int CHAR_THREE_BYTES_MASK = 0xFFFF0000;
    private static final int CHAR_FOUR_BYTES_MASK = 0xFFE00000;
    private static final int CHAR_FIVE_BYTES_MASK = 0xFC000000;
    private static final int CHAR_SIX_BYTES_MASK = 0x80000000;


    public static int countExtraBytes(char character) {
        if ((character & CHAR_ONE_BYTE_MASK) == 0) {
            return 0;
        } else if ((character & CHAR_TWO_BYTES_MASK) == 0) {
            return 1;
        } else if ((character & CHAR_THREE_BYTES_MASK) == 0) {
            return 2;
        } else if ((character & CHAR_FOUR_BYTES_MASK) == 0) {
            return 3;
        } else if ((character & CHAR_FIVE_BYTES_MASK) == 0) {
            return 4;
        } else if ((character & CHAR_SIX_BYTES_MASK) == 0) {
            return 5;
        } else {
            return -1;
        }
    }


    public static int countExtraBytes(char[] textCharacters, int textStart, int textLength) {
        int extraBytes = 0;

        for (int i = 0; i < textLength; i++) {
            extraBytes += countExtraBytes(textCharacters[i + textStart]);
        }

        return extraBytes;
    }
}
