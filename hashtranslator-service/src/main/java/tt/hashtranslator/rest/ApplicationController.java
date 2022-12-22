package tt.hashtranslator.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tt.hashtranslator.model.DecryptRequest;
import tt.hashtranslator.service.ApplicationService;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> sendApplication(@RequestBody DecryptRequest decryptRequest,
                                                  @RequestHeader("Authorization") String authHeader) {
        return applicationService.sendApplicationResponse(decryptRequest, authHeader);
    }
}
