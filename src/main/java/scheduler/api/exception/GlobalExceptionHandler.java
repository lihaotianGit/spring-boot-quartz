package scheduler.api.exception;

import com.google.common.collect.ImmutableMap;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private final static Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map defaultMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        return errInfo(e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map defaultIllegalArgumentExceptionHandler(IllegalArgumentException e) {
        return errInfo(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map defaultExceptionHandler(Exception e) {
        return errInfo(e);
    }

    private Map<String, String> errInfo(Exception e) {
        logger.error("", e);

        String msg = e.getMessage() == null ? "UnknownException" : e.getMessage();
        // TODO: 可能有多个FieldError，该类需要重构，这个判断在这个方法里面不合适
        if (e instanceof MethodArgumentNotValidException) {
            msg = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError().getDefaultMessage();
        }
        return ImmutableMap.<String, String>builder()
                .put("msg", msg)
                .build();
    }

}
