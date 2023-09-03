package az.developia.librarysystemname.response;

import az.developia.librarysystemname.entity.Book;
import az.developia.librarysystemname.entity.User;
import lombok.Data;

@Data
public class LibraryResponse {

    private String name;
    private User user;
    private Book book;

}