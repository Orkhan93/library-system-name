package az.developia.librarysystemname.repository;

import az.developia.librarysystemname.entity.Book;
import az.developia.librarysystemname.wrapper.BookWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    List<BookWrapper> getAllProduct();

    List<Book> findByUser_Id(Long userId);

    List<Book> findByLibrary_Id(Long id);

    List<Book> findByName(String name);

    List<Book> findByPrice(String price);

    List<Book> findByDescription(String description);

    List<Book> findBookByStatus(String status);

    Optional<Book> findByNameAndDescription(String name, String description);

}