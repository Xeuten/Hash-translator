package tt.hashtranslator.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tt.hashtranslator.persistence.DecryptApplicationRepo;
import tt.hashtranslator.util.Utils;

import java.net.http.HttpResponse;

@Service
public class ApplicationResultService {

    @Autowired
    private Utils util;

    @Value("${messages.incorrect_header}")
    private String incorrectHeader;

    /**
     * Like the sendApplicationResponse method, initially this method validates the header that was passed to it via
     * interacting with the authorization service. Then it checks if the application with the id that was passed to it
     * as an argument is in the database. If all the checks were passed, it returns the hashes with the results of their
     * decryption as a JSON.
     * @param id The id of an application.
     * @return The response entity that contains status and JSON of the application.
     */
    @SneakyThrows
    public ResponseEntity<String> applicationResultResponse(String id, String authHeader) {
        if(!util.isHeaderCorrect(authHeader)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(incorrectHeader);
        }
        HttpResponse<String> headerValidationResponse = util.validateHeaderToken(authHeader);
        if(headerValidationResponse.statusCode() != HttpStatus.OK.value()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(headerValidationResponse.body());
        }
        return util.findApplicationById(id);
    }

}
