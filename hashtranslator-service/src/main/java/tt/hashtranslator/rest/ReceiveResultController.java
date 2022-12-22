package tt.hashtranslator.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tt.hashtranslator.service.ReceiveResultService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/receive")
public class ReceiveResultController {

    @Autowired
    private ReceiveResultService receiveResultService;

    @GetMapping
    public String receive(HttpServletRequest request, Model model) {
        return receiveResultService.receiveResponse(request, model);
    }

}
