package utils;

import java.io.UnsupportedEncodingException;

public class StringUtils {
    public static String UTF16BE(String source) throws UnsupportedEncodingException {
        byte[] byteArray = new byte[source.length()/2];
        for(int i = 0; i <  byteArray.length; i += 2) {
            byteArray[i/2] = (byte)Integer.parseInt(source.substring(i, i + 2), 16); 
        }
        String result = new String(byteArray, "UTF-16BE");
        return result;
    }
}
