package app.cafebabe.web.config;

import app.cafebabe.error.ApiException;
import app.cafebabe.model.vo.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class ControllerConfig {
    private ResponseEntity<ApiResp<?>> badRequest(ApiResp<?> apiResp){
        return new ResponseEntity<>(apiResp, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiResp<?>> methodNotAllowed(ApiResp<?> apiResp){
        return new ResponseEntity<>(apiResp, HttpStatus.METHOD_NOT_ALLOWED);
    }

    private ResponseEntity<ApiResp<?>> unauthorized(ApiResp<?> apiResp){
        return new ResponseEntity<>(apiResp, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<ApiResp<?>> notFound(ApiResp<?> apiResp){
        return new ResponseEntity<>(apiResp, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ApiResp<?>> internalError(ApiResp<?> apiResp){
        return new ResponseEntity<>(apiResp, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private void logError(HttpServletRequest r, ApiResp<?> apiResp, Throwable e){
        if(e != null){
            log.error(String.format("%s %s %s", r.getMethod(), r.getRequestURI(), apiResp.getMsg()), e);
        }else{
            log.error("{} {} {}", r.getMethod(), r.getRequestURI(), apiResp.getMsg());
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResp<?>> handle(MissingServletRequestParameterException e, HttpServletRequest r){
        ApiResp<?> resp = ApiResp.error("Missing params", e.getParameterName());
        logError(r, resp, null);
        return badRequest(resp);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResp<?>> handle(ConstraintViolationException e, HttpServletRequest r){
        ApiResp<?> resp = ApiResp.error("Invalid params", e.getMessage());
        logError(r, resp, null);
        return badRequest(resp);
    }


    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResp<?>> handle(ApiException e, HttpServletRequest r){
        ApiResp<?> resp = ApiResp.error(e.getMessage());
        logError(r, resp, null);
        return badRequest(resp);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResp<?>> handle(NoHandlerFoundException e, HttpServletRequest r){
        ApiResp<?> resp = ApiResp.error("Invalid url");
        logError(r, resp, null);
        return notFound(resp);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResp<?>> handle(HttpRequestMethodNotSupportedException e, HttpServletRequest r){
        ApiResp<?> resp = ApiResp.error("Invalid request", e.getMethod());
        logError(r, resp, null);
        return methodNotAllowed(resp);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResp<?>> handle(AccessDeniedException e, HttpServletRequest r){
        ApiResp<?> resp = ApiResp.error("Access denied");
        logError(r, resp, null);
        return unauthorized(resp);
    }


    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ApiResp<?>> handle(FileSizeLimitExceededException e, HttpServletRequest r){
        ApiResp<?> resp = ApiResp.error("Request too large");
        logError(r, resp, null);
        return badRequest(resp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResp<?>> handle(Exception e, HttpServletRequest r){
        ApiResp<?> resp = ApiResp.error("Server error");
        logError(r, resp, e);
        return internalError(resp);
    }
}
