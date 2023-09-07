package az.developia.librarysystemname.response;

import az.developia.librarysystemname.entity.Book;
import az.developia.librarysystemname.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BorrowResponse {

    private LocalDateTime broughtDate;
    private User user;
    private Book book;

}