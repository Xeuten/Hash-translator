package tt.authorization.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public ResponseEntity<String> validationResponse(){
        return ResponseEntity.status(200).body("a");
    }

}
