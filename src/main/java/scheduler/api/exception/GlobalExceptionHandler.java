package scheduler.api.exception;

import com.google.common.collect.ImmutableMap;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private final static Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    // TODO: not found 如何包装?

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
        return ImmutableMap.<String, String>builder()
                .put("msg", e.getMessage())
                .build();
    }

}
