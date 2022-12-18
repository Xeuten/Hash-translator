package tt.authorization.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tt.authorization.service.DeleteUserService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/delete_user")
public class DeleteUserController {

    @Autowired
    private DeleteUserService deleteUserService;

    @GetMapping
    public String deleteUser(@RequestParam String email, HttpServletRequest request, Model model) {
        return deleteUserService.deleteUserResponse(email, request, model);
    }

}