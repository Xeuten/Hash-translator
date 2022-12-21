package tt.authorization.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tt.authorization.service.ValidationService;

@RestController
@RequestMapping("/validation")
public class ValidationController {

    @Autowired
    private ValidationService validationService;

    @PostMapping
    public ResponseEntity<String> validate(@RequestBody String token) {
        return validationService.validationResponse(token);
    }

}
