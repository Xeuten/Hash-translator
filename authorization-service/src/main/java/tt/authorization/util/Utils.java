package tt.authorization.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Base64;

public class Utils {

    /**
     * This method generates a Base64 encoded string from email and password, that were passed to the method as parameters.
     * @param email The email of a user.
     * @param password The password of a user.
     * @return The Base64 encoded token.
     */
    public static String generateBase64Token(String email, String password) {
        return Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
    }

    /**
     * This method checks if the request that was passes to it contains a cookie with the name that was passed to the method.
     * @param request The request that has been sent to the corresponding endpoint.
     * @param cookieName The name of a cookie.
     * @return True, if the cookie was found, and false if it wasn't.
     */
    public static boolean containsCookie(HttpServletRequest request, String cookieName) {
        return Arrays.stream(request.getCookies()).anyMatch(cookie -> cookie.getName().equals(cookieName));
    }

}
