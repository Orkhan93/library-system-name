package az.developia.librarysystemname.service;

import az.developia.librarysystemname.constant.LibraryConstant;
import az.developia.librarysystemname.entity.Library;
import az.developia.librarysystemname.entity.User;
import az.developia.librarysystemname.enums.ErrorCode;
import az.developia.librarysystemname.enums.Role;
import az.developia.librarysystemname.error.ErrorMessage;
import az.developia.librarysystemname.exception.ServiceException;
import az.developia.librarysystemname.mapper.LibraryMapper;
import az.developia.librarysystemname.repository.LibraryRepository;
import az.developia.librarysystemname.repository.UserRepository;
import az.developia.librarysystemname.request.LibraryRegistration;
import az.developia.librarysystemname.request.LibraryRequest;
import az.developia.librarysystemname.response.LibraryResponse;
import az.developia.librarysystemname.util.LibraryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static az.developia.librarysystemname.constant.LibraryConstant.LIBRARY_NOT_FOUND;
import static az.developia.librarysystemname.constant.LibraryConstant.SOMETHING_WENT_WRONG;
import static az.developia.librarysystemname.constant.LibraryConstant.SUCCESSFULLY_DELETED_LIBRARY;
import static az.developia.librarysystemname.constant.LibraryConstant.SUCCESSFULLY_REGISTER;
import static az.developia.librarysystemname.constant.LibraryConstant.UNAUTHORIZED_ACCESS;
import static az.developia.librarysystemname.error.ErrorMessage.USER_ALREADY_EXITS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final LibraryMapper libraryMapper;
    private final EncryptionService encryptionService;

    public ResponseEntity<String> addLibrary(Long userId, LibraryRequest libraryRequest) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> ServiceException.of(ErrorCode.USER_NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND)
        );
        if (user.getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
            if (libraryRepository.findByName(libraryRequest.getName()).isEmpty()) {
                if (validationBookRequest(libraryRequest, false)) {
                    libraryRepository.save(libraryMapper.fromRequestToModel(libraryRequest));
                    return LibraryUtil.getResponseMessage(SUCCESSFULLY_REGISTER, OK);
                } else
                    return LibraryUtil.getResponseMessage(LibraryConstant.INVALID_DATA, BAD_REQUEST);
            } else
                return LibraryUtil.getResponseMessage(LibraryConstant.LIBRARY_ALREADY_EXIST, BAD_REQUEST);
        } else
            return LibraryUtil.getResponseMessage(UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<String> register(LibraryRegistration registrationRequest, Long checkId) {
        User userLibrarian = userRepository.findById(checkId).orElseThrow(
                () -> ServiceException.of(ErrorCode.USER_NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND)
        );
        Optional<User> userStudent = userRepository.findByEmailIgnoreCase(registrationRequest.getEmail());
        if (userLibrarian.getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
            if (userRepository.findByUsernameIgnoreCase(registrationRequest.getUsername()).isPresent() ||
                    userRepository.findByEmailIgnoreCase(registrationRequest.getEmail()).isPresent()) {
                return LibraryUtil.getResponseMessage(USER_ALREADY_EXITS, BAD_REQUEST);
            } else if (userStudent.isEmpty()) {
                userRepository.save(getBookFromRequest(registrationRequest, false));
                return LibraryUtil.getResponseMessage(SUCCESSFULLY_REGISTER, OK);
            } else
                LibraryUtil.getResponseMessage(USER_ALREADY_EXITS, BAD_REQUEST);
        } else
            LibraryUtil.getResponseMessage(UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        return LibraryUtil.getResponseMessage(SOMETHING_WENT_WRONG, INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<Library>> getLibraryByLibrarianId(Long checkId) {
        User userLibrarian = userRepository.findById(checkId).orElseThrow(
                () -> ServiceException.of(ErrorCode.USER_NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND)
        );
        if (userLibrarian != null && userLibrarian.getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
            return new ResponseEntity<>(libraryRepository.findByUsers_Id(checkId), OK);
        } else
            return ResponseEntity.status(UNAUTHORIZED).build();
    }

    public LibraryResponse update(Long checkId, LibraryRequest libraryRequest, Long libraryId) {
        Optional<User> optionalUser = userRepository.findById(checkId);
        Optional<Library> optionalLibrary = libraryRepository.findById(libraryId);
        if (optionalUser.isPresent() && optionalUser.get().getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
            Library library = libraryMapper.fromRequestToModel(libraryRequest);
            library.setId(libraryRequest.getId());
            libraryRepository.save(library);
            return libraryMapper.fromModelToResponse(library);
        } else
            throw ServiceException.of(ErrorCode.UNAUTHORIZED_ACCESS.name(), UNAUTHORIZED_ACCESS);
    }

    public ResponseEntity<String> deleteLibrary(Long checkId, Long libraryId) {
        try {
            Optional<User> optionalUser = userRepository.findById(checkId);
            Optional<Library> optionalLibrary = libraryRepository.findById(libraryId);
            User user = optionalUser.get();
            if (user.getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
                if (optionalLibrary.isPresent() && user.getLibrary().getId() == libraryId) {
                    libraryRepository.deleteById(libraryId);
                    return LibraryUtil.getResponseMessage(SUCCESSFULLY_DELETED_LIBRARY, OK);
                } else
                    return LibraryUtil.getResponseMessage(LIBRARY_NOT_FOUND, BAD_REQUEST);
            } else
                LibraryUtil.getResponseMessage(UNAUTHORIZED_ACCESS, UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibraryUtil.getResponseMessage(SOMETHING_WENT_WRONG, INTERNAL_SERVER_ERROR);
    }

    private boolean validationBookRequest(LibraryRequest libraryRequest, boolean validateId) {
        if (libraryRequest.getName() != null) {
            if (libraryRequest.getId() != null && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private User getBookFromRequest(LibraryRegistration request, boolean isAdd) {
        Library library = new Library();
        library.setId(request.getLibraryId());

        User user = new User();
        if (isAdd) {
            user.setId(request.getId());
        }
        user.setEmail(request.getEmail());
        user.setPassword(encryptionService.encryptPassword(request.getPassword()));
        user.setStatus(request.getStatus());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setUserRole("student");
        user.setLibrary(library);
        return user;
    }

}