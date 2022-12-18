package tt.authorization.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tt.authorization.service.AddUserService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/add_user")
public class AddUserController {

    @Autowired
    private AddUserService addUserService;

    @GetMapping
    public String addUser(@RequestParam String email, @RequestParam String password, HttpServletRequest request,
                          Model model) {
        return addUserService.addUserResponse(email, password, request, model);
    }

}
