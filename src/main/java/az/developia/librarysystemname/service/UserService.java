package az.developia.librarysystemname.service;

import az.developia.librarysystemname.constant.LibraryConstant;
import az.developia.librarysystemname.entity.Library;
import az.developia.librarysystemname.entity.User;
import az.developia.librarysystemname.enums.ErrorCode;
import az.developia.librarysystemname.enums.Role;
import az.developia.librarysystemname.error.ErrorMessage;
import az.developia.librarysystemname.exception.ServiceException;
import az.developia.librarysystemname.exception.UserAlreadyExistException;
import az.developia.librarysystemname.mapper.UserMapper;
import az.developia.librarysystemname.repository.LibraryRepository;
import az.developia.librarysystemname.repository.UserRepository;
import az.developia.librarysystemname.request.UserLoginRequest;
import az.developia.librarysystemname.request.UserRegistrationRequest;
import az.developia.librarysystemname.request.UserRequest;
import az.developia.librarysystemname.response.UserResponse;
import az.developia.librarysystemname.security.JwtUtil;
import az.developia.librarysystemname.util.LibraryUtil;
import az.developia.librarysystemname.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static az.developia.librarysystemname.constant.LibraryConstant.SOMETHING_WENT_WRONG;
import static az.developia.librarysystemname.constant.LibraryConstant.SUCCESSFULLY_USER_DELETED;
import static az.developia.librarysystemname.constant.LibraryConstant.SUCCESSFULLY_USER_UPDATED;
import static az.developia.librarysystemname.constant.LibraryConstant.UNAUTHORIZED_ACCESS;
import static az.developia.librarysystemname.constant.LibraryConstant.USER_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EncryptionService encryptionService;
    private final JwtUtil jwtUtil;
    private final LibraryRepository libraryRepository;

    public UserResponse register(UserRegistrationRequest registrationRequest)
            throws UserAlreadyExistException {
        log.info("Inside register {}", registrationRequest);
        if (userRepository.findByEmailIgnoreCase(registrationRequest.getEmail()).isPresent() ||
                userRepository.findByUsernameIgnoreCase(registrationRequest.getUsername()).isPresent()) {
            throw UserAlreadyExistException.of(ErrorCode.ALREADY_EXIST.name(), ErrorMessage.USER_ALREADY_EXITS);
        }
        if (validationSignup(registrationRequest)) {
            User user = userRepository.save(getUserFromRequest(registrationRequest, false));
            return userMapper.fromModelToResponse(userRepository.save(user));
        } else
            throw UserAlreadyExistException.of(ErrorCode.INTERNAL_SERVER_ERROR.name(), ErrorMessage.SOMETHING_WENT_WRONG);
    }

    public String login(UserLoginRequest loginRequest) {
        log.info("Inside login {}", loginRequest);
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(loginRequest.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (encryptionService.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
                if (user.getStatus().equals("false"))
                    return LibraryConstant.WAIT_FOR_APPROVAL;
                return jwtUtil.generateToken(loginRequest.getEmail(), loginRequest.getUserRole());
            } else
                throw ServiceException.of(ErrorCode.INVALID_DATA.name(), ErrorMessage.INVALID_DATA);
        }
        return LibraryConstant.BAD_CREDENTIALS;
    }

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> ServiceException.of(ErrorCode.USER_NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND));
        return userMapper.fromModelToResponse(user);
    }

    public List<UserWrapper> getUserByFirstName(String firstName) {
        return userMapper.fromModelToWrapper(userRepository.findByFirstName(firstName));
    }

    public List<UserWrapper> getUserByLastName(String lastName) {
        return userMapper.fromModelToWrapper(userRepository.findByLastName(lastName));
    }

    public List<UserWrapper> getUserByStatus(String status) {
        return userMapper.fromModelToWrapper(userRepository.findByStatus(status));
    }

    public ResponseEntity<List<UserWrapper>> getAllUsersByLibraryId(Long checkId, Long libraryId) {
        try {
            Optional<User> optionalUser = userRepository.findById(checkId);
            if (optionalUser.isPresent() && optionalUser.get().getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
                Optional<Library> optionalLibrary = libraryRepository.findById(libraryId);
                if (optionalLibrary.isPresent()) {
                    return new ResponseEntity<>
                            (userMapper.fromModelToWrapper(userRepository.findByLibrary_Id(libraryId)), OK);
                } else
                    return ResponseEntity.status(NOT_FOUND).build();
            } else
                return ResponseEntity.status(UNAUTHORIZED).build();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), INTERNAL_SERVER_ERROR);
    }

    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        log.info("Inside userRequest {}", userRequest);
        User findUser = userRepository.findById(userId).orElseThrow(
                () -> ServiceException.of(ErrorCode.USER_NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND)
        );
        User user = userMapper.fromToRequestToModel(userRequest);
        user.setId(findUser.getId());
        user.setPassword(encryptionService.encryptPassword(userRequest.getPassword()));
        userRepository.save(user);
        return userMapper.fromModelToResponse(user);
    }

    public ResponseEntity<String> updateStatus(Long userId, UserRegistrationRequest registrationRequest) {
        log.info("Inside updateStatus {}", registrationRequest);
        try {
            Optional<User> findUser = userRepository.findById(userId);
            User user = findUser.get();
            if (user.getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
                Optional<User> optionalUser = userRepository.findById(registrationRequest.getId());
                if (optionalUser.isPresent()) {
                    userRepository.updateStatus(registrationRequest.getStatus(), Math.toIntExact(registrationRequest.getId()));
                    return LibraryUtil.getResponseMessage
                            (SUCCESSFULLY_USER_UPDATED, OK);
                } else {
                    LibraryUtil.getResponseMessage(USER_NOT_FOUND, OK);
                }
            } else {
                return LibraryUtil.getResponseMessage(UNAUTHORIZED_ACCESS, UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibraryUtil.getResponseMessage(SOMETHING_WENT_WRONG, INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<UserResponse> updateUserByLibrarian(Long checkedId, Long userId, UserRequest userRequest) {
        log.info("Inside updateUserByLibrarian {}", userRequest);
        Optional<User> findUser = userRepository.findById(checkedId);
        User refUser = findUser.get();
        if (refUser.getUserRole().equalsIgnoreCase(Role.LIBRARIAN.name())) {
            if (userRequest.getId() == userId) {
                Optional<User> optionalUser = userRepository.findById(userId);
                if (optionalUser.isPresent()) {
                    Library library = libraryRepository.findById(userRequest.getLibraryId()).orElseThrow(
                            () -> ServiceException.of(ErrorCode.LIBRARY_NOT_FOUND.name(), ErrorMessage.LIBRARY_NOT_FOUND)
                    );
                    User user = userMapper.fromToRequestToModel(userRequest);
                    user.setId(userRequest.getId());
                    user.setLibrary(library);
                    user.setPassword(encryptionService.encryptPassword(userRequest.getPassword()));
                    return ResponseEntity.ok(userMapper.fromModelToResponse(userRepository.save(user)));
                } else
                    throw ServiceException.of(ErrorCode.USER_NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND);
            }
        } else
            throw ServiceException.of(ErrorCode.UNAUTHORIZED_ACCESS.name(), ErrorMessage.UNAUTHORIZED_ACCESS);
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    public ResponseEntity<String> delete(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(optionalUser.get().getId());
            return LibraryUtil.getResponseMessage(SUCCESSFULLY_USER_DELETED, OK);
        } else
            throw ServiceException.of(ErrorCode.USER_NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND);
    }

    private boolean validationSignup(UserRegistrationRequest registrationRequest) {
        log.info("Inside request {}", registrationRequest);
        if (registrationRequest.getUsername() != null && registrationRequest.getPassword() != null
                && registrationRequest.getEmail() != null && registrationRequest.getFirstName() != null
                && registrationRequest.getLastName() != null) {
            return true;
        }
        return false;
    }

    private User getUserFromRequest(UserRegistrationRequest registrationRequest, boolean isAdd) {
        Library library = new Library();
        library.setId(registrationRequest.getLibraryId());

        User user = new User();
        if (isAdd) {
            user.setId(registrationRequest.getId());
        }
        user.setLibrary(library);
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(encryptionService.encryptPassword(registrationRequest.getPassword()));
        user.setUserRole("student");
        user.setStatus("false");
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        return user;
    }

}