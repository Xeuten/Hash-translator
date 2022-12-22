package tt.hashtranslator.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @Value("${messages.incorrect_json}")
    private String incorrectJson;

    @Value("${messages.login_error}")
    private String loginError;

    /**
     * This handler is called when conversion of the user's input into DecryptRequest object has failed.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(400).body(incorrectJson);
    }

    /**
     * This handler is called when the user tries to access the URLs which are mapped to POST requests with a
     * GET request.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(400).body(loginError);
    }

    /**
     * This handler is called when the user tries to access the URLs which require an authorization header.
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> handleHeaderMissing(MissingRequestHeaderException ex) {
        return ResponseEntity.status(400).body(loginError);
    }

}
