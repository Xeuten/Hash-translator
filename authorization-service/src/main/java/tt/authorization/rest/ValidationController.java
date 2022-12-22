package tt.authorization.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tt.authorization.service.ValidationService;

@RestController
@RequestMapping("/validation")
public class ValidationController {

    @Autowired
    private ValidationService validationService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<String> validate(@RequestHeader("Authorization") String authHeader) {
        return validationService.validationResponse(authHeader);
    }

}
