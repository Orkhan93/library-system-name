package az.developia.librarysystemname.service;

import az.developia.librarysystemname.constant.LibraryConstant;
import az.developia.librarysystemname.entity.User;
import az.developia.librarysystemname.enums.ErrorCode;
import az.developia.librarysystemname.error.ErrorMessage;
import az.developia.librarysystemname.exception.ServiceException;
import az.developia.librarysystemname.exception.UserAlreadyExistException;
import az.developia.librarysystemname.mapper.UserMapper;
import az.developia.librarysystemname.repository.UserRepository;
import az.developia.librarysystemname.request.UserLoginRequest;
import az.developia.librarysystemname.request.UserRegistrationRequest;
import az.developia.librarysystemname.response.UserResponse;
import az.developia.librarysystemname.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;
    private final JwtUtil jwtUtil;

    public UserResponse register(UserRegistrationRequest registrationRequest)
            throws UserAlreadyExistException {
        log.info("Inside register {}", registrationRequest);
        if (userRepository.findByEmailIgnoreCase(registrationRequest.getEmail()).isPresent() ||
                userRepository.findByUsernameIgnoreCase(registrationRequest.getUsername()).isPresent()) {
            throw UserAlreadyExistException.of(ErrorCode.ALREADY_EXIST.name(), ErrorMessage.USER_ALREADY_EXITS);
        }
        if (validationSignup(registrationRequest)) {
            User user = userMapper.fromUserRegisterRequestToModel(registrationRequest);
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
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

    private boolean validationSignup(UserRegistrationRequest registrationRequest) {
        log.info("Inside request {}", registrationRequest);
        if (registrationRequest.getUsername() != null && registrationRequest.getPassword() != null
                && registrationRequest.getEmail() != null && registrationRequest.getFirstName() != null
                && registrationRequest.getLastName() != null) {
            return true;
        }
        return false;
    }

}