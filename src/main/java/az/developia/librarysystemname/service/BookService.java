package az.developia.librarysystemname.service;

import az.developia.librarysystemname.entity.Book;
import az.developia.librarysystemname.entity.User;
import az.developia.librarysystemname.enums.Role;
import az.developia.librarysystemname.repository.BookRepository;
import az.developia.librarysystemname.repository.UserRepository;
import az.developia.librarysystemname.request.BookRequest;
import az.developia.librarysystemname.security.JwtRequestFilter;
import az.developia.librarysystemname.util.LibraryUtil;
import az.developia.librarysystemname.wrapper.BookWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static az.developia.librarysystemname.constant.LibraryConstant.ADDED_SUCCESSFULLY;
import static az.developia.librarysystemname.constant.LibraryConstant.INVALID_DATA;
import static az.developia.librarysystemname.constant.LibraryConstant.SOMETHING_WENT_WRONG;
import static az.developia.librarysystemname.constant.LibraryConstant.UNAUTHORIZED_ACCESS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ResponseEntity<String> addBook(BookRequest bookRequest) {
        log.info("Inside bookRequest {}", bookRequest);
        Optional<User> optionalUser = userRepository.findById(bookRequest.getUserId());
        User user = optionalUser.get();
        try {
            if (user.getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
                if (validationBookRequest(bookRequest, false)) {
                    bookRepository.save(getBookFromRequest(bookRequest, false));
                    return LibraryUtil.getResponseMessage(ADDED_SUCCESSFULLY, OK);
                }
                return LibraryUtil.getResponseMessage(INVALID_DATA, BAD_REQUEST);
            } else
                return LibraryUtil.getResponseMessage(UNAUTHORIZED_ACCESS, UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibraryUtil.getResponseMessage(SOMETHING_WENT_WRONG, INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<BookWrapper>> getAllBook() {
        try {
            return new ResponseEntity<>(bookRepository.getAllProduct(), OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), INTERNAL_SERVER_ERROR);
    }


    private Book getBookFromRequest(BookRequest bookRequest, boolean isAdd) {
        User user = new User();
        user.setId(bookRequest.getUserId());

        Book book = new Book();
        if (isAdd) {
            book.setId(bookRequest.getId());
        } else {
            book.setStatus("true");
        }
        book.setUser(user);
        book.setName(bookRequest.getName());
        book.setDescription(bookRequest.getDescription());
        book.setPrice(bookRequest.getPrice());
        return book;
    }

    private boolean validationBookRequest(BookRequest bookRequest, boolean validateId) {
        if (bookRequest.getName() != null && bookRequest.getPrice() != null && bookRequest.getDescription() != null) {
            if (bookRequest.getId() != null && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

}