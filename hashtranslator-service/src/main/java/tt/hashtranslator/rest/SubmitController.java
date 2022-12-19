package tt.hashtranslator.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tt.hashtranslator.service.SubmitService;

@Controller
@RequestMapping("/api/submit")
public class SubmitController {

    @Autowired
    private SubmitService submitService;

    @GetMapping
    public String submitHashes(@ModelAttribute("base64t") String token, Model model) {
        return submitService.submitResponse(token, model);
    }
}
