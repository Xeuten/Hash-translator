package tt.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tt.authorization.persistence.UserRepository;

import java.util.Base64;

@Service
public class ValidationService {

    @Autowired
    private UserRepository userRepository;

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
        String[] headerParts = authHeader.split(" ");
        if(!headerParts[0].equals("Basic") || headerParts.length != 2) {
            return ResponseEntity.status(400).body(incorrectHeader);
        }
        String[] decodedCredentials = new String(Base64.getDecoder().decode(headerParts[1])).split(":");
        if(decodedCredentials.length == 2
                && userRepository.credentialsAreCorrect(decodedCredentials[0], decodedCredentials[1])) {
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.status(401).body(errorMessage);
    }

}
