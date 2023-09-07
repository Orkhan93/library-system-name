package az.developia.librarysystemname.service;

import az.developia.librarysystemname.entity.Book;
import az.developia.librarysystemname.entity.Borrow;
import az.developia.librarysystemname.entity.User;
import az.developia.librarysystemname.enums.ErrorCode;
import az.developia.librarysystemname.enums.Role;
import az.developia.librarysystemname.error.ErrorMessage;
import az.developia.librarysystemname.exception.ServiceException;
import az.developia.librarysystemname.mapper.BorrowMapper;
import az.developia.librarysystemname.repository.BookRepository;
import az.developia.librarysystemname.repository.BorrowRepository;
import az.developia.librarysystemname.repository.UserRepository;
import az.developia.librarysystemname.request.BorrowRequest;
import az.developia.librarysystemname.response.BorrowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowMapper borrowMapper;

    public BorrowResponse broughtBook(BorrowRequest borrowRequest, Long checkId) {
        Optional<User> optionalLibrarian = userRepository.findById(checkId);
        Optional<Book> optionalBook = bookRepository.findById(borrowRequest.getBookId());
        if (optionalLibrarian.isPresent() && optionalLibrarian.get().getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
            if (optionalBook.isPresent() && optionalBook.get().getStatus().equalsIgnoreCase("true")) {
                User student = userRepository.findById(borrowRequest.getUserId()).orElseThrow(
                        () -> ServiceException.of(ErrorCode.USER_NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND));
                Borrow borrow = borrowMapper.requestToModel(borrowRequest);
                borrow.setBook(optionalBook.get());
                borrow.setUser(student);
                Borrow findBorrow = borrowRepository.findByBook_Id(borrow.getBook().getId());
                if (Objects.isNull(findBorrow)) {
                    return borrowMapper.modelToResponse(borrowRepository.save(borrow));
                } else
                    throw ServiceException.of(ErrorCode.INVALID_DATA.name(), ErrorMessage.INVALID_DATA);
            } else
                throw ServiceException.of(ErrorCode.BOOK_NOT_FOUND.name(), ErrorMessage.BOOK_NOT_FOUND);
        } else
            throw ServiceException.of(ErrorCode.UNAUTHORIZED_ACCESS.name(), ErrorMessage.UNAUTHORIZED_ACCESS);
    }

    public List<BorrowResponse> getBroughtBooksByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> ServiceException.of(ErrorCode.USER_NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND));
        return borrowMapper.listModelToListResponse(borrowRepository.findByUser_Id(user.getId()));
    }

}