package az.developia.librarysystemname.wrapper;

import az.developia.librarysystemname.entity.Book;
import az.developia.librarysystemname.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryWrapper {

    private Long id;
    private String name;
    private List<User> users;
    private List<Book> books;

    public LibraryWrapper(Long id, String name, List<Book> books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }

}