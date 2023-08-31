package az.developia.librarysystemname.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWrapper {

    private Long id;
    private String username;

    @JsonIgnore
    private String password;

    private String email;
    private String firstName;
    private String lastName;
    private String status;
    private String userRole;

}