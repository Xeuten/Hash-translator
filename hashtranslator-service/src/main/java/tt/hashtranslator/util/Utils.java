package tt.hashtranslator.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Utils {

    /**
     * This method builds a HTTP request with POST type of method, with the specified URL, body and authorization token,
     * inserted as an authorization header.
     * @param URL Request is sent to this URL.
     * @param requestBody The body of the request.
     * @param headerToken The token that is inserted to request's authorization header.
     * @return The HTTP POST request with all specified parameters.
     * @throws URISyntaxException
     */
    public static HttpRequest buildPostRequest(String URL, String requestBody, String headerToken) throws URISyntaxException {
        return HttpRequest.newBuilder().uri(new URI(URL)).headers("Content-Type", "application/json", "Authorization",
                "Basic " + headerToken).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
    }

    /**
     * This method builds a HTTP request with GET type of method, with the specified URL and authorization token,
     * inserted as an authorization header.
     * @param URL Request is sent to this URL.
     * @param headerToken The token that is inserted to request's authorization header.
     * @return The HTTP GET request with all specified parameters.
     * @throws URISyntaxException
     */
    public static HttpRequest buildGetRequest(String URL, String headerToken) throws URISyntaxException {
        return HttpRequest.newBuilder().uri(new URI(URL)).headers("Authorization", "Basic " + headerToken).GET().build();
    }

    /**
     * This method builds a HTTP request with GET type of method, with the specified URL and request parameters that
     * were passed to the method as a hash map.
     * @param URL Request is sent to this URL.
     * @param requestParams The hash map of the request parameters.
     * @return The HTTP GET request with all specified parameters.
     * @throws URISyntaxException
     */
    public static HttpRequest buildGetRequestWithParams(String URL, Map<String, String> requestParams) throws URISyntaxException {
        String newURL = URL + "?" + String.join("&", (requestParams.keySet().stream()
                .map(key -> key + "=" + requestParams.get(key)).toList()));
        return HttpRequest.newBuilder().uri(new URI(newURL)).headers("Content-Type", "application/json").GET().build();
    }

    /**
     * This method receives a hash and creates a map of the format, which is required to make a request to the website,
     * where the hash is decrypted.
     * @param hash The hash to decrypt.
     * @return The map of the required format.
     */
    public static Map<String, String> mapOfParams(String hash) {
        HashMap<String, String> params = new HashMap<>();
        params.put("hash", hash);
        params.put("hash_type", "md5");
        params.put("email", "bogimoogu@gmail.com");
        params.put("code", "9cab78bb777932a8");
        return params;
    }

    /**
     * This method checks if the client has the cookie, which contains the authorization token and returns
     * the Optional of the value of the cookie.
     * @param request The object of a class that implements HttpServletRequest interface.
     * @return The Optional, which contains the value of the cookie, if the client has it.
     */
    public static Optional<String> getToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("base64t"))
                .findFirst().map(Cookie::getValue);
    }

}
