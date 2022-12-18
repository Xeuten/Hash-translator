package tt.authorization.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Base64;

public class Utils {
    public static String generateBase64Token(String eMail, String password) {
        return Base64.getEncoder().encodeToString(("email=" + eMail + "&password=" + password).getBytes());
    }

    public static boolean containsCookie(HttpServletRequest request, String cookieName) {
        return Arrays.stream(request.getCookies()).anyMatch(cookie -> cookie.getName().equals(cookieName));
    }

}
