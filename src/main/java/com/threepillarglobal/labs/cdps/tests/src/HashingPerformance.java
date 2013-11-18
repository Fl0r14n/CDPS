/**
 * Created with IntelliJ IDEA.
 * User: soros
 * Date: 11/15/13
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */

import com.google.gwt.util.tools.shared.Md5Utils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class HashingPerformance {

    public static String messageDigestMD5Hash(String userName) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(userName.getBytes());
        byte[] theDigest = md.digest();

        return new String(Hex.encodeHex(theDigest));

    }

    public static String commonsMd5Hash(String userName) throws Exception {

        return DigestUtils.md5Hex(userName);

    }

    public static String messageDigestSHA1Hash(String userName) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.reset();
        md.update(userName.getBytes());
        byte[] theDigest = md.digest();

        return new String(Hex.encodeHex(theDigest));

    }

    public static String commonsSHA1Hash(String userName) throws Exception {

        return DigestUtils.shaHex(userName);

    }

    public static String gwtMD5Hash(String userName) throws UnsupportedEncodingException {
        byte[] ba = Md5Utils.getMd5Digest(userName.getBytes("UTF-8"));
        return new String(Hex.encodeHex(ba));
    }

}