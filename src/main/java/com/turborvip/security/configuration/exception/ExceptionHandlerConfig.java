package doctintuc.com.websitedoctintuc.config.exception;

import doctintuc.com.websitedoctintuc.adapter.web.base.RestData;
import doctintuc.com.websitedoctintuc.adapter.web.base.VsResponseUtil;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionHandlerConfig {

    private static final Log LOG = LogFactory.getLog(ExceptionHandlerConfig.class);

    private final MessageSource messageSource;//a interface provide multipart language

    public ExceptionHandlerConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(value = {InvalidException.class})
    protected ResponseEntity<RestData<?>> handleInvalidException(InvalidException ex) {
        String resolvedReason = this.messageSource.getMessage(ex.getUserMessage(), null, LocaleContextHolder.getLocale());
        return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, resolvedReason, ex.getMessage());
    }

    @ExceptionHandler(value = {VsException.class})
    protected ResponseEntity<RestData<?>> handleVsException(VsException ex) {
        return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<RestData<?>> handConstraintException(ConstraintViolationException ex) {
        String message;
        try {
        //LocalContextHolder.getLocale() : get java object present ,use define language and country that application is using
        message = ex.getMessage();
        } catch (Exception exception) {
            message = exception.getMessage();
        }
        return VsResponseUtil.error(HttpStatus.BAD_REQUEST, message,
                ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
    }

    /*@ExceptionHandler(value = {BindException.class})
    protected ResponseEntity<RestData<?>> handBindingException(BindException ex) {
        String message = "";
        if (ex.getFieldError() != null) {
            try {
                message = messageSource.getMessage(Objects.requireNonNull(ex.getFieldError().getDefaultMessage()),
                        null, LocaleContextHolder.getLocale());
            } catch (Exception exception) {
                message = messageSource.getMessage("An error occurred", null, LocaleContextHolder.getLocale());
            }
        }
        return VsResponseUtil.error(HttpStatus.BAD_REQUEST, message,
                ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
    }
*/
    @ExceptionHandler(value = {HttpClientErrorException.Forbidden.class})
    protected ResponseEntity<RestData<?>> handlerRefreshTokenException(HttpClientErrorException.Forbidden ex) {
        LOG.error(ex.getMessage(), ex);
        String message;
        try {
            message = messageSource.getMessage(Objects.requireNonNull(ex.getMessage()), null, LocaleContextHolder.getLocale());
        } catch (Exception exception) {
            message = exception.getMessage();
        }
        return VsResponseUtil.error(HttpStatus.FORBIDDEN, message, ex.getLocalizedMessage());

    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<RestData<?>> handleException(Exception exception) {
        LOG.error(exception.getCause(), exception);
        if (exception.getCause() instanceof VsException) {
            VsException vsException = (VsException) exception;
            String message = messageSource.getMessage(exception.getMessage(), null, LocaleContextHolder.getLocale());
            return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, message, vsException.getDevMessage());
        }

        if (exception.getCause() instanceof InvalidException) {
            VsException invalidException = (VsException) exception;
            String message = messageSource.getMessage(exception.getMessage(), null, LocaleContextHolder.getLocale());
            return VsResponseUtil.error(HttpStatus.BAD_REQUEST, message, invalidException.getDevMessage());
        }

        String message = messageSource.getMessage("An error occurred", null, LocaleContextHolder.getLocale());
        return VsResponseUtil.error(HttpStatus.BAD_REQUEST, message,
                exception.getCause() != null ? exception.getCause().getMessage() : exception.getMessage());
    }
}
