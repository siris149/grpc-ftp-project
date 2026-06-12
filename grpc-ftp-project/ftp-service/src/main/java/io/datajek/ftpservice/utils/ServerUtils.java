package io.datajek.ftpservice.utils;

import com.google.protobuf.ByteString;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * This class holds utility methods.
 */

public class ServerUtils {
    static public boolean isNullOrEmpty(String string) {
        return "".equals(string) || string == null;
    }

    /**
     * Returns false if the hash of the received data doesn't match with the
     * hash computed on the client side for the same chunk of data.
     *
     * @param content
     * @param receivedHash
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static boolean doChecksumsMatch(ByteString content, ByteString receivedHash)
            throws NoSuchAlgorithmException {
        byte[] computedHash = MessageDigest.getInstance("MD5").digest(content.toByteArray());
        return Arrays.equals(computedHash, receivedHash.toByteArray());
    }

}