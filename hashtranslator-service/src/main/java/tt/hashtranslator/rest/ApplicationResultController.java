package tt.hashtranslator.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tt.hashtranslator.service.ApplicationResultService;

@RestController
@RequestMapping("/api/applications/{id}")
public class ApplicationResultController {

    @Autowired
    private ApplicationResultService applicationResultService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<String> getResult(@PathVariable String id, @RequestHeader("Authorization") String authHeader) {
        return applicationResultService.applicationResultResponse(id, authHeader);
    }

}
