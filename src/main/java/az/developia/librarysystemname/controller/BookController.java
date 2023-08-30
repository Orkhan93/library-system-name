package az.developia.librarysystemname.controller;

import az.developia.librarysystemname.entity.Book;
import az.developia.librarysystemname.entity.User;
import az.developia.librarysystemname.request.BookRequest;
import az.developia.librarysystemname.response.BookResponse;
import az.developia.librarysystemname.service.BookService;
import az.developia.librarysystemname.util.LibraryUtil;
import az.developia.librarysystemname.wrapper.BookWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static az.developia.librarysystemname.constant.LibraryConstant.SOMETHING_WENT_WRONG;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@Valid @RequestBody BookRequest bookRequest) {
        try {
            return bookService.addBook(bookRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibraryUtil.getResponseMessage(SOMETHING_WENT_WRONG, INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get")
    public ResponseEntity<List<BookWrapper>> getAllBook() {
        try {
            return bookService.getAllBook();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{userId}/update/{bookId}")
    public ResponseEntity<BookResponse> updateBook(@AuthenticationPrincipal User user, @PathVariable Long userId,
                                                      @PathVariable Long bookId, @RequestBody BookRequest bookRequest) {
        return bookService.updateBook(user, userId, bookId, bookRequest);
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId) {
        try {
            return bookService.deleteBook(bookId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibraryUtil.getResponseMessage(SOMETHING_WENT_WRONG, INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<List<BookWrapper>> getAllBookByUserId(@PathVariable(name = "userId") Long userId) {
        try {
            return bookService.getAllBookByUserId(userId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), INTERNAL_SERVER_ERROR);
    }

}