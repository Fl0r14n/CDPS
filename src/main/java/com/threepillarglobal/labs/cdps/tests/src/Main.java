/**
 * Created with IntelliJ IDEA.
 * User: soros
 * Date: 11/15/13
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */

import com.google.gwt.thirdparty.guava.common.base.Stopwatch;

public class Main {
    public static void main(String[] agrs) {
        try {

            String userName = "UserName";
            Stopwatch timer = new Stopwatch().start();
            String hashStr;
            for (int i = 0; i < 1000000; i++) {
                hashStr = HashingPerformance.commonsMd5Hash(userName + Integer.toString(i));
            }
            timer.stop();
            System.out.println("MD5 hash using apache commons took " + timer.elapsedMillis() + " ms");

            timer.start();
            for (int i = 0; i < 1000000; i++) {
                hashStr = HashingPerformance.messageDigestMD5Hash(userName + Integer.toString(i));
            }
            timer.stop();
            System.out.println("MD5 hash using java message digest took " + timer.elapsedMillis() + " ms");

            timer.start();
            for (int i = 0; i < 1000000; i++) {
                hashStr = HashingPerformance.gwtMD5Hash(userName + Integer.toString(i));
            }
            timer.stop();
            System.out.println("MD5 hash using gwt took " + timer.elapsedMillis() + " ms");

            timer.start();
            for (int i = 0; i < 1000000; i++) {
                hashStr = HashingPerformance.commonsSHA1Hash(userName + Integer.toString(i));
            }
            timer.stop();
            System.out.println("SHA-1 hash using apache commons took " + timer.elapsedMillis() + " ms");

            timer.start();
            for (int i = 0; i < 1000000; i++) {
                hashStr = HashingPerformance.messageDigestSHA1Hash(userName + Integer.toString(i));
            }
            timer.stop();
            System.out.println("SHA-1 hash using java message digest took " + timer.elapsedMillis() + " ms");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}