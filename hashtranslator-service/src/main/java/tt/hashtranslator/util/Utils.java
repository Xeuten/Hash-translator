package tt.hashtranslator.util;

import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tt.hashtranslator.model.DecryptApplication;
import tt.hashtranslator.model.DecryptApplicationStatus;
import tt.hashtranslator.model.DecryptRequest;
import tt.hashtranslator.persistence.DecryptApplicationRepo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
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

    @Value("${urls.decrypt_site}")
    private String decryptSite;

    @Value("${data.error_code}")
    private String code;

    @Value("${urls.applications}")
    private String applicationsUrl;

    @Value("${messages.application_submitted}")
    private String applicationSubmitted;

    @Value("${messages.login_error}")
    private String loginError;

    @Value("${messages.incorrect_format}")
    private String incorrectFormat;

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
        return HttpClient.newHttpClient().send(buildGetRequest(validationUrl, authHeader.split(" ")[1]),
                HttpResponse.BodyHandlers.ofString());
    }

    /**
     * This method checks if the decryption application with specified id is in the database. If it is, the method
     * returns the result. Otherwise, it returns the entity with the error message.
     * @param id The id of an application.
     * @return A response entity with "OK" status and hashes with their decryptions in JSON format, if the application
     * was found, or a response entity with "NOT FOUND" status if otherwise.
     */
    public ResponseEntity<String> findApplicationResponse(String id) {
        return decryptApplicationRepo.findById(id)
                .map(decryptApplication -> ResponseEntity.status(HttpStatus.OK)
                        .body(new GsonBuilder()
                                        .setPrettyPrinting()
                                        .create()
                                        .toJson(Map.of("hashes", decryptApplication.getMapHash()))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format(applicationNotExists, id)));
    }

    /**
     * This method loops through all the hashes that the decrypt request contains, decrypting them and saving the
     * results in the database. Then it returns the id of the application.
     * @param decryptRequest The decryption request is essentially the list of hashes to decrypt.
     * @return The id of the decryption application.
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public String decryptAllAndSave(DecryptRequest decryptRequest)
            throws URISyntaxException, IOException, InterruptedException {
        DecryptApplication application = new DecryptApplication(decryptRequest);
        boolean allDecrypted = true;
        HashMap<String, String> updatedMapHash = application.getMapHash();
        for(String hash : decryptRequest.hashes) {
            HttpResponse<String> decryptResponse = decryptHash(hash);
            if(isDecryptSuccessful(decryptResponse)) {
                updatedMapHash.put(hash, decryptResponse.body());
            } else {
                allDecrypted = false;
            }
        }
        application.setMapHash(updatedMapHash);
        application.setStatus(allDecrypted ? DecryptApplicationStatus.DECRYPTED : DecryptApplicationStatus.PROCESSED);
        decryptApplicationRepo.save(application);
        return application.getId();
    }

    /**
     * This method sends a request to the exterior website that tries to decrypt a hash and receives a response.
     * @param hash The hash to decrypt.
     * @return The response that was returned by the website.
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpResponse<String> decryptHash(String hash)
            throws URISyntaxException, IOException, InterruptedException {
        return HttpClient.newHttpClient().send(buildGetRequestWithParams(decryptSite, mapOfParams(hash)),
                HttpResponse.BodyHandlers.ofString());
    }

    /**
     * This method checks if the decryption that the exterior website tried to make was successful.
     * @param decryptResponse The response of the website.
     * @return True if the decryption was successful and false if it wasn't.
     */
    public boolean isDecryptSuccessful(HttpResponse<String> decryptResponse) {
        String body = decryptResponse.body();
        return decryptResponse.statusCode() == HttpStatus.OK.value()
                && !(body.length() == 16 && body.substring(0,13).equals(code))
                && !body.equals("");
    }

    /**
     * This method creates a cookie with expiration age of 30 minutes, with a name "base64t", and with a value, which
     * equals encoded credentials.
     * @param response HttpServletResponse object that stores user's token.
     * @param token User's base64 encoded credentials.
     */
    public void createUserCookie(HttpServletResponse response, String token) {
        Cookie tokenCookie = new Cookie(tokenName, token);
        tokenCookie.setMaxAge(1800);
        response.addCookie(tokenCookie);
    }

    /**
     * This method checks if the data that was passed to it as an argument has correct format, and if the user's token
     * is present. If both checks were passed, it sends a request with user's input to one of the main controllers.
     * @param isSubmit This boolean value indicates which controller received the request - the submission controller
     * or the reception controller.
     * @param rawData The string is expected to contain user's data in raw form.
     * @param request HttpServletRequest object that is expected to store user's token.
     * @return The response from a controller or the message that corresponds to the error that occurred.
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public String receiveResponseFromMainControllers(boolean isSubmit, String rawData, HttpServletRequest request)
            throws URISyntaxException, IOException, InterruptedException {
        Optional<String> token = getToken(request);
        return rawData.split("=").length != 2 ?
                incorrectFormat :
                token.isEmpty() ?
                        loginError :
                        sendAndReceive(isSubmit, rawData.split("=")[1], token.get());
    }

    /**
     * This method sends requests to one of the main controllers and returns its response.
     * @param isSubmit This boolean value indicates which controller received the request - the submission controller
     * or the reception controller.
     * @param data The string is expected to contain either the JSON of hashes to decrypt, or the id of an
     * application.
     * @param token User's base64 encoded credentials.
     * @return The response from a controller.
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public String sendAndReceive(boolean isSubmit, String data, String token)
            throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> applicationResponse = HttpClient.newHttpClient().send(isSubmit ?
                        buildPostRequest(applicationsUrl, data, token) :
                        buildGetRequest(applicationsUrl + "/" + data, token),
                HttpResponse.BodyHandlers.ofString());
        return applicationResponse.statusCode() == HttpStatus.ACCEPTED.value() ?
                applicationSubmitted + applicationResponse.body() :
                applicationResponse.body();
    }

}