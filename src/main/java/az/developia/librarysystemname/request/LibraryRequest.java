package az.developia.librarysystemname.request;

import az.developia.librarysystemname.entity.Book;
import az.developia.librarysystemname.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryRequest {

    private Long id;

    @NotNull
    @NotBlank
    private String name;

    private User user;
    private Book book;

}