package az.developia.librarysystemname.repository;

import az.developia.librarysystemname.entity.Book;
import az.developia.librarysystemname.wrapper.BookWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<BookWrapper> getAllProduct();

}