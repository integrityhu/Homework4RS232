package utils;

public class ArrayUtils {
    public static int find(byte[] buffer, int value) {
        int result = -1;
        for(int idx=0; (idx < buffer.length) && (result < 0) ; idx++) {
            if (buffer[idx] == value) {
                result = idx;
            }
        }
        return result;
    }
}
