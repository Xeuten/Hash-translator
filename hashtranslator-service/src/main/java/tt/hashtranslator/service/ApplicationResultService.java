package tt.hashtranslator.service;

import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tt.hashtranslator.model.DecryptApplication;
import tt.hashtranslator.persistence.DecryptApplicationRepo;
import tt.hashtranslator.util.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Optional;

@Service
public class ApplicationResultService {

    @Autowired
    private DecryptApplicationRepo decryptApplicationRepo;

    @Value("${urls.validation}")
    private String validationUrl;

    @Value("${messages.validation_send_failed}")
    private String validationSendFailed;

    @Value("${messages.url_error}")
    private String urlError;

    @Value("${messages.incorrect_header}")
    private String incorrectHeader;

    @Value("${messages.application_not_exists}")
    private String applicationNotExists;

    /**
     * Like the sendApplicationResponse method, initially this method validates the header that was passed to it via
     * interacting with the authorization service. Then it checks if the application with the id that was passed to it
     * as an argument is in the database. If all the checks were passed, it returns the hashes with the results of their
     * decryption as a JSON.
     * @param id The id of an application.
     * @return The response entity that contains status and JSON of the application.
     */
    public ResponseEntity<String> applicationResultResponse(String id, String authHeader) {
        String[] headerParts = authHeader.split(" ");
        if(!headerParts[0].equals("Basic") || headerParts.length != 2) {
            return ResponseEntity.status(400).body(incorrectHeader);
        }
        try {
            HttpResponse<String> headerValidationResponse = HttpClient.newHttpClient().send(Utils
                    .buildGetRequest(validationUrl, headerParts[1]), HttpResponse.BodyHandlers.ofString());
            if(headerValidationResponse.statusCode() == 200) {
                Optional<DecryptApplication> application = decryptApplicationRepo.findById(id);
                if (application.isPresent()) {
                    HashMap<String, Object> output = new HashMap<>();
                    HashMap<String, String> mapHash = application.get().getMapHash();
                    output.put("hashes", mapHash);
                    return ResponseEntity.status(200).body(new GsonBuilder()
                            .setPrettyPrinting()
                            .create()
                            .toJson(output));
                }
                return ResponseEntity.status(400).body(String.format(applicationNotExists, id));
            }
            return ResponseEntity.status(400).body(headerValidationResponse.body());
        } catch(IOException | InterruptedException e) {
            return ResponseEntity.status(400).body(validationSendFailed);
        } catch(URISyntaxException e) {
            return ResponseEntity.status(400).body(urlError);
        }
    }
}
