package tt.hashtranslator.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tt.hashtranslator.service.ReceptionService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/receive")
public class ReceptionController {

    @Autowired
    private ReceptionService receptionService;

    @GetMapping
    public String receive(HttpServletRequest request, Model model) {
        return receptionService.receptionResponse(request, model);
    }

}
