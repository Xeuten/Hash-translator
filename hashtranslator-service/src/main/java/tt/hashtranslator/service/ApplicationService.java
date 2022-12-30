package tt.hashtranslator.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tt.hashtranslator.model.DecryptRequest;
import tt.hashtranslator.util.Utils;

import java.net.http.HttpResponse;

@Service
public class ApplicationService {

    @Autowired
    private Utils util;

    @Value("${messages.incorrect_header}")
    private String incorrectHeader;

    /**
     * This method checks format of the header that was passed to it as an argument and then validates it. If both the
     * checks were passed, it tries to decrypt all the hashes, saves them in the database and returns the id of the
     * application.
     * @param decryptRequest The decryption request is essentially the list of hashes to decrypt.
     * @param authHeader The authorization header.
     * @return The response entity that contains id of the application, or if an error occurred, the response
     * entity that contains error message.
     */
    @SneakyThrows
    public ResponseEntity<String> sendApplicationResponse(DecryptRequest decryptRequest, String authHeader) {
        if(!util.isHeaderCorrect(authHeader)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(incorrectHeader);
        }
        HttpResponse<String> headerValidationResponse = util.validateHeaderToken(authHeader);
        if(headerValidationResponse.statusCode() != HttpStatus.OK.value()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headerValidationResponse.body());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(util.decryptAllAndSave(decryptRequest));
    }

}
