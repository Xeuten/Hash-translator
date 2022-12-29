package tt.hashtranslator.util;

import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tt.hashtranslator.persistence.DecryptApplicationRepo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class Utils {

    @Autowired
    private DecryptApplicationRepo decryptApplicationRepo;

    @Value("${data.decrypt_email}")
    private String decryptEmail;

    @Value("${data.decrypt_code}")
    private String decryptCode;

    @Value("${data.token_name}")
    private String tokenName;

    @Value("${urls.validation}")
    private String validationUrl;

    @Value("${messages.application_not_exists}")
    private String applicationNotExists;

    /**
     * This method builds a HTTP request with POST type of method, with the specified URL, body and authorization token,
     * inserted as an authorization header.
     * @param URL Request is sent to this URL.
     * @param requestBody The body of the request.
     * @param headerToken The token that is inserted to request's authorization header.
     * @return The HTTP POST request with all specified parameters.
     * @throws URISyntaxException
     */
    public HttpRequest buildPostRequest(String URL, String requestBody, String headerToken)
            throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URI(URL))
                .headers("Content-Type", "application/json", "Authorization", "Basic " + headerToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

    /**
     * This method builds a HTTP request with GET type of method, with the specified URL and authorization token,
     * inserted as an authorization header.
     * @param URL Request is sent to this URL.
     * @param headerToken The token that is inserted to request's authorization header.
     * @return The HTTP GET request with all specified parameters.
     * @throws URISyntaxException
     */
    public HttpRequest buildGetRequest(String URL, String headerToken)
            throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URI(URL))
                .headers("Authorization", "Basic " + headerToken)
                .GET()
                .build();
    }

    /**
     * This method builds a HTTP request with GET type of method, with the specified URL and request parameters that
     * were passed to the method as a hash map.
     * @param URL Request is sent to this URL.
     * @param requestParams The hash map of the request parameters.
     * @return The HTTP GET request with all specified parameters.
     * @throws URISyntaxException
     */
    public HttpRequest buildGetRequestWithParams(String URL, Map<String, String> requestParams)
            throws URISyntaxException {
        String newURL = URL + "?" + String.join("&", (requestParams
                .keySet()
                .stream()
                .map(key -> key + "=" + requestParams.get(key))
                .toList()));
        return HttpRequest.newBuilder()
                .uri(new URI(newURL))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
    }

    /**
     * This method receives a hash and creates a map of the format, which is required to make a request to the website,
     * where the hash is decrypted.
     * @param hash The hash to decrypt.
     * @return The map of the required format.
     */
    public Map<String, String> mapOfParams(String hash) {
        return Map.of(
                "hash", hash,
                "hash_type","md5",
                "email",decryptEmail,
                "code",decryptCode);
    }

    /**
     * This method checks if the client has the cookie, which contains the authorization token and returns
     * the Optional of the value of the cookie.
     * @param request The object of a class that implements HttpServletRequest interface.
     * @return The Optional, which contains the value of the cookie, if the client has it.
     */
    public Optional<String> getToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(tokenName))
                .findFirst()
                .map(Cookie::getValue);
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
     * This method sends a request to the controller of authorization service, where the authorization
     * header is validated. Then it receives the response.
     * @param authHeader Authorization header.
     * @return The response that was received from authorization service.
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpResponse<String> validateHeaderToken(String authHeader)
            throws URISyntaxException, IOException, InterruptedException {
        return HttpClient.newHttpClient().send(
                buildGetRequest(validationUrl, authHeader.split(" ")[1]),
                HttpResponse.BodyHandlers.ofString());
    }

    /**
     * This method checks if the decryption application with specified id is in the database. If it is, the method
     * returns the result. Otherwise, it returns the entity with the error message.
     * @param id The id of an application.
     * @return A response entity with "OK" status and hashes with their decryptions in JSON format, if the application
     * was found, or a response entity with "NOT FOUND" status if otherwise.
     */
    public ResponseEntity<String> findApplicationById(String id) {
        return decryptApplicationRepo.findById(id).map(decryptApplication ->
                        ResponseEntity.status(HttpStatus.OK)
                                .body(new GsonBuilder()
                                        .setPrettyPrinting()
                                        .create()
                                        .toJson(Map.of("hashes", decryptApplication.getMapHash()))))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(String.format(applicationNotExists, id)));
    }

}