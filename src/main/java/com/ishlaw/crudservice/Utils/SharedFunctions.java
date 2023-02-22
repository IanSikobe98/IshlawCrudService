package com.ishlaw.crudservice.Utils;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
@Service
@Slf4j
public class SharedFunctions {

    @Autowired
    Environment environment;


    private static final Logger LOGGER = LoggerFactory.getLogger(SharedFunctions.class);

    public SharedFunctions(Environment environment) {
        this.environment = environment;
    }

    public SharedFunctions() {
    }


    /**
     * Uses modern more secure algorithm PBKDF2
     *
     * @param password   //     * @param salt
     * @param iterations
     * @param keyLength
     * @return
     */
    public static String hashPassword(final char[] password, final byte[] salt, final int iterations, final int keyLength) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return toHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

}
