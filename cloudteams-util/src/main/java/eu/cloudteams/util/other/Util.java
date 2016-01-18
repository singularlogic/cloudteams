package eu.cloudteams.util.other;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christos Paraskeva
 */
public class Util {

    /**
     * Encodes a String based on a specific algorithm
     *
     * @param content The String to encrypt
     * @param algorithm The algorithm to be used
     * @return The encoded String
     */
    public static String createAlgorithm(String content, String algorithm) {
        String encryptedContent = "";
        try {
            //Producing the SHA hash for the input
            MessageDigest m;
            m = MessageDigest.getInstance(algorithm);
            m.update(content.getBytes(), 0, content.length());
            encryptedContent = new BigInteger(1, m.digest()).toString(16);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Util.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return encryptedContent;
    }

    public static String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numchars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }

    /**
     *
     * This method converts an Inputstream to Byte Array
     *
     * @param _is
     *
     * @return The byte array of the given inputstream
     *
     */
    public static byte[] convertInputStreamToByteArray(InputStream _is) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[0];

            while ((nRead = _is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            return buffer.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
