package tt.hashtranslator.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import tt.hashtranslator.service.SubmissionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @GetMapping
    public String submitHashes(@ModelAttribute("base64t") String token, HttpServletResponse response,
                               HttpServletRequest request, Model model) {
        return submissionService.submissionResponse(token, response, request, model);
    }
}
