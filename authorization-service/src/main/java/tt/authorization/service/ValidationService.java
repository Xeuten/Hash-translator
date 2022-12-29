package tt.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tt.authorization.util.Utils;

@Service
public class ValidationService {

    @Autowired
    private Utils util;

    @Value("${messages.incorrect_credentials}")
    private String errorMessage;

    @Value("${messages.login_error}")
    private String loginError;

    @Value("${messages.incorrect_header}")
    private String incorrectHeader;

    /**
     * This method receives an authentication header, which consists of the words "Basic" and a Base64 encoded token of
     * email and password of a user. If the credentials are in the base, the response is code 200. If they aren't, the
     * response is code 401 and the error message.
     * @param authHeader The string that is interpreted as th encoded token.
     * @return The response entity that contains status.
     */
    public ResponseEntity<String> validationResponse(String authHeader) {
        if(!util.isHeaderCorrect(authHeader)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(incorrectHeader);
        }
        if(!util.areCredentialsValid(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
