package az.developia.librarysystemname.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAlreadyExistException extends Exception {

    private String code;
    private String message;

    public UserAlreadyExistException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static UserAlreadyExistException of(String code, String message) {
        return new UserAlreadyExistException(code, message);
    }

}