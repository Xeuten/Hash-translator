package tt.hashtranslator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import tt.hashtranslator.model.DecryptionRequest;
import tt.hashtranslator.util.Utils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Service
public class ApplicationService {

    @Value("${messages.absent_auth}")
    private String absentAuth;

    @Value("${messages.incorrect_header}")
    private String incorrectHeader;

    @Value("${messages.validation_send_failed}")
    private String validationSendFailed;

    @Value("${urls.validation}")
    private String validationUrl;

    public ResponseEntity<String> sendApplicationResponse(DecryptionRequest decryptionRequest, @Nullable String authHeader) {
        if(authHeader == null) {
            return ResponseEntity.status(400).body(absentAuth);
        }
        String[] headerParts = authHeader.split(" ");
        if(!headerParts[0].equals("Basic") || headerParts.length != 2) {
            return ResponseEntity.status(400).body(incorrectHeader);
        }
        try {
            HttpResponse<String> headerValidationResponse = HttpClient.newHttpClient()
                    .send(Utils.buildPostRequest(validationUrl, headerParts[1], null), HttpResponse.BodyHandlers.ofString());
            return ResponseEntity.status(200).body("test");
            //            if(headerValidationResponse.statusCode() == 200) {
//
//            }
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(400).body(validationSendFailed);
        }
    }

}
