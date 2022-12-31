package tt.authorization.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tt.authorization.model.User;
import tt.authorization.persistence.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;

@Component
public class Utils {

    @Autowired
    private UserRepository userRepository;

    @Value("${data.admin_token_name}")
    private String adminTokenName;

    /**
     * This method generates a Base64 encoded string from email and password, that were passed to the method as parameters.
     * @param email The email of a user.
     * @param password The password of a user.
     * @return The Base64 encoded token.
     */
    public String generateBase64Token(String email, String password) {
        return Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
    }

    /**
     * This method checks if the request that was passes to it contains a cookie with the name that was passed to the method.
     * @param request The request that has been sent to the corresponding endpoint.
     * @param cookieName The name of a cookie.
     * @return True, if the cookie was found, and false if it wasn't.
     */
    public boolean containsCookie(HttpServletRequest request, String cookieName) {
        return Arrays.stream(request.getCookies()).anyMatch(cookie -> cookie.getName().equals(cookieName));
    }

    /**
     * This method creates a cookie with expiration age of 30 minutes, with a name "base64AdminToken", and with a value, which equals to admins'
     * credentials encoded as a Base64 token.
     * @param response HttpServletResponse object that stores admins' token.
     * @param admin User object instantiated with admins' credentials.
     */
    public void createAdminCookie(HttpServletResponse response, User admin) {
        Cookie adminTokenCookie = new Cookie(adminTokenName, generateBase64Token(admin.getEmail(), admin.getPassword()));
        adminTokenCookie.setMaxAge(1800);
        response.addCookie(adminTokenCookie);
    }

    /**
     * This method checks if authorization header has the correct format.
     * @param authHeader Authorization header.
     * @return True if the header is correct and false if it isn't.
     */
    public boolean isHeaderCorrect(String authHeader) {
        String[] headerParts = authHeader.split(" ");
        return headerParts[0].equals("Basic") && headerParts.length == 2;
    }

    /**
     * This method checks if the credentials encoded into header are in the database.
     * @param authHeader Authorization header.
     * @return True if the credentials are correct and false if they aren't.
     */
    public boolean areCredentialsValid(String authHeader) {
        String[] decodedCredentials = new String(Base64.getDecoder()
                .decode(authHeader.split(" ")[1]))
                .split(":");
        return decodedCredentials.length == 2
                && !userRepository.findByEmailAndPassword(decodedCredentials[0], decodedCredentials[1]).isEmpty();
    }

}
