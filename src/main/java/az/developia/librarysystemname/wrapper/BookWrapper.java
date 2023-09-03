package az.developia.librarysystemname.wrapper;

import az.developia.librarysystemname.entity.Library;
import az.developia.librarysystemname.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookWrapper {

    private Long id;
    private String name;
    private String description;
    private String price;
    private String status;

    @JsonIgnore
    private User user;

    private Library library;

}