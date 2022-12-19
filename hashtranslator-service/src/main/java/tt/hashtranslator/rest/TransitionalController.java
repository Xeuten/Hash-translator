package tt.hashtranslator.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tt.hashtranslator.service.TransitionalService;

@Controller
@RequestMapping("/transitional")
public class TransitionalController {

    @Autowired
    private TransitionalService transitionalService;

    @PostMapping
    public String sendApplication(@RequestBody String decryptionRequest, Model model) {
        return transitionalService.transitionalResponse(decryptionRequest, model);
    }

}
