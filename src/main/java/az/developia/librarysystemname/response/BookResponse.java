package az.developia.librarysystemname.response;

import az.developia.librarysystemname.entity.Library;
import az.developia.librarysystemname.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookResponse {

    private Long id;
    private String name;
    private String description;
    private String price;
    private String status;

    @JsonIgnore
    private User user;

    @JsonIgnore
    private Library library;

}