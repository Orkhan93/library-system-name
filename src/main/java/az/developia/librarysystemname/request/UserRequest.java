package az.developia.librarysystemname.request;

import lombok.Data;

@Data
public class UserRequest {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private final String userRole;
    private final String status;

}