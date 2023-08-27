package az.developia.librarysystemname.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LibraryUtil {

    private LibraryUtil() {

    }

    public static ResponseEntity<String> getResponseMessage(String responseMessage , HttpStatus httpStatus) {
        return new  ResponseEntity<String>(responseMessage,httpStatus);
    }

}