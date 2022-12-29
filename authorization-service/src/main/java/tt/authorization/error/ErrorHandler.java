package tt.authorization.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @Value("${messages.login_error}")
    private String loginError;

    /**
     * This handler is invoked when either the user tries to access the URLs that are mapped to POST requests with a
     * GET request, or the user tries to access the URLs that require an authorization header.
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, MissingRequestHeaderException.class})
    public ResponseEntity<String> handleMethodNotSupportedAndMissingHeader(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginError);
    }

}
