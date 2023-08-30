package az.developia.librarysystemname.error;

import az.developia.librarysystemname.enums.ErrorCode;
import az.developia.librarysystemname.exception.ServiceException;
import az.developia.librarysystemname.exception.UserAlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCustomException(ServiceException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ex.getCode());
        errorResponse.setMessage(ex.getMessage());
        log.info("Service Exception {}", ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentTypeMismatchException ex) {

        String parameterName = ex.getParameter().getParameter().getName();

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ErrorCode.VALIDATION_ERROR.name());
        errorResponse.setMessage(parameterName + ErrorMessage.VALIDATION_ERROR);
        log.error("Validation server error : {}", ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEmptyResultDataAccess(EmptyResultDataAccessException ex) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ErrorCode.VALIDATION_ERROR.name());
        errorResponse.setMessage(ErrorMessage.USER_NOT_FOUND);
        log.error("Validation server error : {}", ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentNotValidException(MethodArgumentNotValidException ex) {

        String fieldName = ex.getBindingResult().getFieldError().getField();

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ErrorCode.VALIDATION_ERROR.name());
        errorResponse.setMessage(fieldName + ErrorMessage.VALIDATION_ERROR);
        log.error("Validation error : {}", ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleUnknownException(Exception e) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ErrorCode.INTERNAL_SERVER_ERROR.name());
        errorResponse.setMessage(ErrorMessage.INTERNAL_SERVER_ERROR);
        log.error("Internal server error : {}", e.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ErrorResponse handleUserAlreadyExistException(UserAlreadyExistException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ErrorCode.ALREADY_EXIST.name());
        errorResponse.setMessage(ErrorMessage.USER_ALREADY_EXITS);
        log.error("User already exist {}", ex.getMessage());
        return errorResponse;
    }

}