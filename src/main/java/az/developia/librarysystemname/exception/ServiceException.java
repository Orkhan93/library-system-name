package az.developia.librarysystemname.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceException extends RuntimeException {

    private String code;
    private String message;

    public ServiceException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ServiceException of(String code, String message) {
        return new ServiceException(code, message);
    }

}