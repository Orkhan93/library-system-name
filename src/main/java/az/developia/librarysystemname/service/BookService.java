package az.developia.librarysystemname.service;

import az.developia.librarysystemname.entity.Book;
import az.developia.librarysystemname.entity.Library;
import az.developia.librarysystemname.entity.User;
import az.developia.librarysystemname.enums.Role;

import az.developia.librarysystemname.error.ErrorMessage;
import az.developia.librarysystemname.exception.ServiceException;
import az.developia.librarysystemname.mapper.BookMapper;
import az.developia.librarysystemname.repository.BookRepository;
import az.developia.librarysystemname.repository.LibraryRepository;
import az.developia.librarysystemname.repository.UserRepository;
import az.developia.librarysystemname.request.BookAddRequest;
import az.developia.librarysystemname.request.BookRequest;
import az.developia.librarysystemname.response.BookResponse;

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
import static az.developia.librarysystemname.constant.LibraryConstant.BOOK_ALREADY_EXIST;
import static az.developia.librarysystemname.constant.LibraryConstant.DOES_NOT_EXIST;
import static az.developia.librarysystemname.constant.LibraryConstant.SOMETHING_WENT_WRONG;
import static az.developia.librarysystemname.constant.LibraryConstant.SUCCESSFULLY_BOOK_DELETED;
import static az.developia.librarysystemname.constant.LibraryConstant.SUCCESSFULLY_DELETED;
import static az.developia.librarysystemname.constant.LibraryConstant.UNAUTHORIZED_ACCESS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LibraryRepository libraryRepository;
    private final BookMapper bookMapper;

    public ResponseEntity<String> addBook(BookAddRequest bookRequest) {
        log.info("Inside bookRequest {}", bookRequest);
        Optional<User> optionalUser = userRepository.findById(bookRequest.getUserId());
        User user = optionalUser.get();
        try {
            if (user.getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
                Optional<Book> optionalBook =
                        bookRepository.findByNameAndDescription(bookRequest.getName(), bookRequest.getDescription());
                if (validationBookRequest(bookRequest, false) && optionalBook.isEmpty()) {
                    bookRepository.save(getBookFromRequest(bookRequest, false));
                    return LibraryUtil.getResponseMessage(ADDED_SUCCESSFULLY, OK);
                }
                return LibraryUtil.getResponseMessage(BOOK_ALREADY_EXIST, BAD_REQUEST);
            } else
                return LibraryUtil.getResponseMessage(UNAUTHORIZED_ACCESS, UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibraryUtil.getResponseMessage(SOMETHING_WENT_WRONG, INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<BookResponse> updateBook(User user, Long userId, Long bookId, BookRequest bookRequest) {
        Optional<User> findUser = userRepository.findById(userId);
        User refUser = findUser.get();
        if (refUser.getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
            if (bookRequest.getId() == bookId) {
                Optional<Book> optionalBook = bookRepository.findById(bookId);
                if (optionalBook.isPresent()) {
                    Optional<Library> optionalLibrary = libraryRepository.findById(optionalBook.get().getLibrary().getId());
                    User optionalUser = optionalBook.get().getUser();
                    if (optionalUser.getId() == userId && optionalLibrary.isPresent()) {
                        Library library = optionalLibrary.get();
                        bookRequest.setLibrary(library);
                        bookRequest.setUser(optionalUser);
                        return ResponseEntity.ok(bookMapper.fromModelToResponse
                                (bookRepository.save(bookMapper.fromRequestToModel(bookRequest))));
                    }
                } else
                    throw ServiceException.of(ErrorCode.BOOK_NOT_FOUND.name(), ErrorMessage.BOOK_NOT_FOUND);
            }
        } else
            throw ServiceException.of(ErrorCode.UNAUTHORIZED_ACCESS.name(), ErrorMessage.UNAUTHORIZED_ACCESS);
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    public BookResponse getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> ServiceException.of(ErrorCode.BOOK_NOT_FOUND.name(), ErrorMessage.BOOK_NOT_FOUND));
        return bookMapper.fromModelToResponse(book);
    public ResponseEntity<Book> updateBook(User user, Long userId, Long bookId, BookRequest bookRequest) {
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isPresent() && findUser.get().getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
            if (bookRequest.getId() == bookId) {
                Optional<Book> optionalBook = bookRepository.findById(bookId);
                if (optionalBook.isPresent()) {
                    User optionalUser = optionalBook.get().getUser();
                    if (optionalUser.getId() == userId) {
                        bookRequest.setUserId(optionalUser.getId());
                        return ResponseEntity.ok(bookRepository.save(bookMapper.fromRequestToModel(bookRequest)));
                    }
                }
            }
        }


//        Optional<User> optionalUser = userRepository.findById(bookRequest.getUserId());
//        User user = optionalUser.get();
//        try {
//            if (user.getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
//                if (validationBookRequest(bookRequest, true)) {
//                    Optional<Book> optionalBook = bookRepository.findById(bookId);
//                    if (optionalBook.isPresent()) {
//                        Book book = new Book();
//                        book.setName(bookRequest.getName());
//                        book.setPrice(bookRequest.getPrice());
//                        book.setDescription(bookRequest.getDescription());
//                        bookRepository.save(book);
//                        return LibraryUtil.getResponseMessage(SUCCESSFULLY_UPDATED, OK);
//                    } else
//                        LibraryUtil.getResponseMessage(DOES_NOT_EXIST, OK);
//                } else
//                    LibraryUtil.getResponseMessage(INVALID_DATA, BAD_REQUEST);
//            } else
//                return LibraryUtil.getResponseMessage(UNAUTHORIZED_ACCESS, UNAUTHORIZED);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return LibraryUtil.getResponseMessage(SOMETHING_WENT_WRONG, INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    public ResponseEntity<String> deleteBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            bookRepository.deleteById(id);
            return LibraryUtil.getResponseMessage(SUCCESSFULLY_BOOK_DELETED, OK);
        }
        return LibraryUtil.getResponseMessage(DOES_NOT_EXIST, NOT_FOUND);
    }

    public ResponseEntity<List<BookWrapper>> getAllBook() {
        //TODO checked getAll
        try {
            return new ResponseEntity<>(bookRepository.getAllProduct(), OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), INTERNAL_SERVER_ERROR);
    }

    public List<BookWrapper> getBookByName(String name) {
        return bookMapper.fromModelToWrapper(bookRepository.findByName(name));
    }

    public List<BookWrapper> getBookByPrice(String price) {
        return bookMapper.fromModelToWrapper(bookRepository.findByPrice(price));
    }

    public List<BookWrapper> getBookByDescription(String description) {
        return bookMapper.fromModelToWrapper(bookRepository.findByDescription(description));
    }

    public List<BookWrapper> getBookByStatus(String status) {
        return bookMapper.fromModelToWrapper(bookRepository.findBookByStatus(status));
    }

    public ResponseEntity<List<BookWrapper>> getAllBookByUserId(Long userId) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent() && optionalUser.get().getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
                return new ResponseEntity<>(bookMapper.fromModelToWrapper(bookRepository.findByUser_Id(userId)), OK);
            } else
                return ResponseEntity.status(UNAUTHORIZED).build();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<BookWrapper>> getAllBookByLibraryId(Long libraryId) {
        try {
            Optional<Library> optionalLibrary = libraryRepository.findById(libraryId);
            if (optionalLibrary.isPresent()) {
                return new ResponseEntity<>(bookMapper.fromModelToWrapper(bookRepository.findByLibrary_Id(libraryId)), OK);
            } else
                return ResponseEntity.status(NOT_FOUND).build();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), INTERNAL_SERVER_ERROR);
    }

    private Book getBookFromRequest(BookAddRequest bookRequest, boolean isAdd) {
        User user = new User();
        user.setId(bookRequest.getUserId());

        Library library = new Library();
        library.setId(bookRequest.getLibraryId());

        Book book = new Book();
        if (isAdd) {
            book.setId(bookRequest.getId());
        } else {
            book.setStatus("true");
        }
        book.setUser(user);
        book.setLibrary(library);
        book.setName(bookRequest.getName());
        book.setDescription(bookRequest.getDescription());
        book.setPrice(bookRequest.getPrice());
        return book;
    }

    private boolean validationBookRequest(BookAddRequest bookRequest, boolean validateId) {
        if (bookRequest.getName() != null && bookRequest.getPrice() != null && bookRequest.getDescription() != null) {
            if (bookRequest.getId() != null && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    public boolean userHasPermission(User user, Long id) {
        return user.getId() == id;
    }

}