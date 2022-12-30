package tt.hashtranslator.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import tt.hashtranslator.util.Utils;

import javax.servlet.http.HttpServletRequest;

@Service
public class TransitionalService {

    @Autowired
    private Utils util;

    /**
     * This method corresponds to the controller, which acts as a bridge from the controllers that view to user the
     * input forms to the controllers which process the user's input. It sends the requests to them and adds their
     * responses to the View.
     * @param isSubmit The boolean value adjusts the method, depending on which of the controllers has called it.
     * @param rawData The variable contain either a raw JSON of hashes to submit, or a raw id of an application.
     * @param request The request that has been sent to the corresponding endpoint.
     * @param model The model that will be passed to the View.
     * @return The string that corresponds to the general Thymeleaf html template
     */
    @SneakyThrows
    public String transitionalResponse(boolean isSubmit, String rawData, HttpServletRequest request, Model model) {
        model.addAttribute("message", util.receiveResponseFromMainControllers(isSubmit, rawData, request));
        return "template1";
    }

}
