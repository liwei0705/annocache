package com.gitee.cs_liwei.annocache.utils;

import org.apache.commons.codec.digest.DigestUtils;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class MD5Util {

    public static String MD5(String sourceStr) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(sourceStr.getBytes());
            byte[] md = mdInst.digest();

            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int tmp = md[i];
                if (tmp < 0)
                    tmp += 256;
                if (tmp < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(tmp));
            }

            return buf.toString().toUpperCase();// 32bit
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String MD5NoUpper(String sourceStr) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(sourceStr.getBytes());
            byte[] md = mdInst.digest();

            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int tmp = md[i];
                if (tmp < 0)
                    tmp += 256;
                if (tmp < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(tmp));
            }

            return buf.toString();// 32bit
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String md5(String text,String charset) {
        try {
            if (charset == null)
                return DigestUtils.md5Hex(text);
            return DigestUtils.md5Hex(text.getBytes(charset));
        } catch (UnsupportedEncodingException e1) {
            return null;
        }
    }
}
