package tt.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tt.authorization.persistence.UserRepository;

import java.util.Base64;
import java.util.UUID;

@Service
public class ValidationService {

    @Autowired
    private UserRepository userRepository;

    @Value("${messages.incorrect_credentials}")
    private String errorMessage;

    public ResponseEntity<String> validationResponse(String token){
        String[] decodedCredentials = new String(Base64.getDecoder().decode(token)).split(":");
        if(userRepository.credentialsAreCorrect(decodedCredentials[0], decodedCredentials[1])) {
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.status(401).body(errorMessage);
    }

}
