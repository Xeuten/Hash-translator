package tt.hashtranslator.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import tt.hashtranslator.service.SubmitService;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api")
public class SubmitController {

    @Autowired
    private SubmitService submitService;

    @GetMapping
    public String submitHashes(@ModelAttribute("base64t") String token, HttpServletResponse response, Model model) {
        return submitService.submitResponse(token, response, model);
    }
}
