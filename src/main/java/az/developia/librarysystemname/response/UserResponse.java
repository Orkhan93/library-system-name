package az.developia.librarysystemname.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

}