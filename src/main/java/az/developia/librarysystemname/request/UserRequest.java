package az.developia.librarysystemname.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private final String userRole = "user";

}