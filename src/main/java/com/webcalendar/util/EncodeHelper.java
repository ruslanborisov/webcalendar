package com.webcalendar.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encode password of the user before it will be added to databases
 * Using md5 algoritm for encode password.
 *
 *
 * @author Ruslan Borisov
 */
public class EncodeHelper implements PasswordEncoder {

    private MessageDigest md;

    public EncodeHelper() {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String encode(CharSequence rawPassword) {

        if(md==null) {
            return rawPassword.toString();
        }

        md.update(rawPassword.toString().getBytes());

        byte byteDate[] = md.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i<byteDate.length; i++) {
            String hex = Integer.toHexString(0xff & byteDate[i]);
            if (hex.length()==1)
                hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        return encode(rawPassword).equals(encodedPassword);
    }
}
