package utils;

import java.net.URL;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class Validation {

    public static boolean isUrlValid(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
