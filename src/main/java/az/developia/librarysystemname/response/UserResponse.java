package az.developia.librarysystemname.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {

    private String username;

    @JsonIgnore
    private String password;

    private String email;
    private String firstName;
    private String lastName;

}