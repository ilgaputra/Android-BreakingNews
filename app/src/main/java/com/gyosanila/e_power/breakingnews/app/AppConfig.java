package com.gyosanila.e_power.breakingnews.app;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AppConfig {
    public static final String sources = "cnn";
    public static final String ApiKey = "148887467cf54019a8c6e03a3c646b24";
    public static final String Server = "https://newsapi.org/v2/top-headlines?sources="+sources+"&apiKey="+ApiKey;

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
