package tt.hashtranslator.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import tt.hashtranslator.service.TransitionalService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/transitional")
public class SubmissionTransitionalController {

    @Autowired
    private TransitionalService transitionalService;

    @PostMapping
    public String sendApplication(@RequestBody String decryptionRequest, HttpServletRequest request, Model model) {
        return transitionalService.transitionalResponse(true, decryptionRequest, request, model);
    }

}
