package tt.authorization.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import tt.authorization.model.User;
import tt.authorization.service.DashboardService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @PostMapping
    public RedirectView toDashboard(@ModelAttribute("user") User user, RedirectAttributes redirAtr,
                                    HttpServletResponse response) {
        return dashboardService.dashboardResponse(user, redirAtr, response);
    }

}
